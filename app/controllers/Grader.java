package controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony on 6/24/15.
 */
public class Grader {
    private static List<Request> history = new ArrayList<Request>();

    public static void storeAndExecute(Request req) {
        history.add(req); // optional
        req.execute();
    }
}
