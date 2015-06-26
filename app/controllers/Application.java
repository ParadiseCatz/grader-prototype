package controllers;

import models.Container;
import models.Grader;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.history;
import views.html.index;
import views.html.success;

import java.io.File;

public class Application extends Controller {

    public Result history() {
        return ok(history.render(Grader.history));
    }

    public Result index() {
        return ok(index.render());
    }

    public Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart submission = body.getFile("submission");
        if (submission != null) {
            String fileName = submission.getFilename();
            File file = submission.getFile();
            File destination = new File("/tmp/box/", fileName);
            destination.getParentFile().mkdirs();
            file.renameTo(destination);
            new Thread() {
                public void run() {
                    Container container = new Container(destination);
                    Request gradingRequest = new GradingRequest(container);
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
