package controllers;

import models.Submission;

/**
 * Created by anthony on 6/24/15.
 */
public interface Request {
    Submission getSubmission();

    int getId();

    void execute();

}
