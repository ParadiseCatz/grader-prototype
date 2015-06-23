package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.index;

import java.io.File;

public class Application extends Controller {

    public Result index() {
        return ok(index.render());
    }

    public Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart submission = body.getFile("picture");
        if (submission != null) {
            String fileName = submission.getFilename();
            String contentType = submission.getContentType();
            File file = submission.getFile();
            File destination = new File("/tmp/", fileName);
            file.renameTo(destination);
            return ok("File uploaded");
        } else {
            flash("error", "Missing file");
            return redirect(routes.Application.index());
        }
    }
}
