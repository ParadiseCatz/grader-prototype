package controllers;

import models.Grader;
import models.Submission;
import models.ZipSubmission;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FilenameUtils;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.history;
import views.html.index;
import views.html.submission;
import views.html.success;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application extends Controller {
    File temporaryStorage;

    private static File compressTemplate() {
        try {
            ZipFile zipFile = new ZipFile("template.zip");
            if (Files.deleteIfExists(Paths.get("template.zip"))) {
                zipFile = new ZipFile("template.zip");
            }

            ZipParameters parameters = new ZipParameters();

            // COMP_DEFLATE is for compression
            // COMP_STORE no compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            // DEFLATE_LEVEL_ULTRA = maximum compression
            // DEFLATE_LEVEL_MAXIMUM
            // DEFLATE_LEVEL_NORMAL = normal compression
            // DEFLATE_LEVEL_FAST
            // DEFLATE_LEVEL_FASTEST = fastest compression
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);

            // file compressed
            for (File templateFile : new File("template").listFiles()) {
                zipFile.addFile(templateFile, parameters);
            }

            return new File("template.zip");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Result history() {
        return ok(history.render(Grader.history));
    }

    public Result index() {
        return ok(index.render());
    }

    public Result template() {
        return ok(compressTemplate());
    }

    public Result submission(int id) {
        return ok(submission.render(Grader.history.get(id).getTestcases()));
    }

    public Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart submissionFile = null;
        if (body != null) {
            submissionFile = body.getFile("submission");
        }
        if (submissionFile != null) {
            String fileName = submissionFile.getFilename();
            File file = submissionFile.getFile();
            temporaryStorage = new File("/tmp/box/", fileName);
            temporaryStorage.getParentFile().mkdirs();
            file.renameTo(temporaryStorage);

            new Thread() {
                public void run() {
                    Submission submission;
                    if (FilenameUtils.getExtension(temporaryStorage.getName()).equals("zip")) {
                        submission = new ZipSubmission(temporaryStorage);
                    } else {
                        submission = new Submission(temporaryStorage);
                    }
                    Request gradingRequest = new GradingRequest(submission);
                    Grader.storeAndExecute(gradingRequest);
                }
            }.start();
            return ok(success.render());
        } else {
            flash("error", "Missing file");
            return redirect(routes.Application.index());
        }
    }
}
