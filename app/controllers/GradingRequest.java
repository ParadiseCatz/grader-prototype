package controllers;

import models.Container;
import models.Submission;

/**
 * Created by anthony on 6/24/15.
 */
public class GradingRequest implements Request {
    private Submission submission;
    private int id;

    public GradingRequest(Submission submission) {
        this.submission = submission;
        this.id = submission.id;
    }

    @Override
    public Submission getSubmission() {
        return submission;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override    // Command
    public void execute() {
        new Container(submission);
    }
}
