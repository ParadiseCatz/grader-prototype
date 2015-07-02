package models;

import controllers.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony on 6/24/15.
 */
public class Grader {
    public static List<Submission> history = new ArrayList<>();

    public static void storeAndExecute(Request req) {
        history.add(req.getSubmission());
        req.execute();
    }
}
