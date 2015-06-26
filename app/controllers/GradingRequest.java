package controllers;

import models.Container;

/**
 * Created by anthony on 6/24/15.
 */
public class GradingRequest implements Request {
    private Container container;
    private int id;

    public GradingRequest(Container container) {
        this.container = container;
        this.id = container.getId();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override    // Command
    public void execute() {
        container.init();
    }
}
