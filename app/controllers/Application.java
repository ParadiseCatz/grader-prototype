package controllers;

import models.Grader;
import models.Submission;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.history;
import views.html.index;
import views.html.submission;
import views.html.success;

import java.io.File;

public class Application extends Controller {
    File temporaryStorage;

    public Result history() {
        return ok(history.render(Grader.history));
    }

    public Result index() {
        return ok(index.render());
    }

    public Result submission(int id) {
        return ok(submission.render(Grader.history.get(id).getTestcases()));
    }

    public Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart submissionFile = body.getFile("submission");
        if (submissionFile != null) {
            String fileName = submissionFile.getFilename();
            File file = submissionFile.getFile();
            temporaryStorage = new File("/tmp/box/", fileName);
            temporaryStorage.getParentFile().mkdirs();
            file.renameTo(temporaryStorage);

            new Thread() {
                public void run() {
                    Submission submission = new Submission(temporaryStorage);
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
