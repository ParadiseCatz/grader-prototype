package controllers;

/**
 * Created by anthony on 6/24/15.
 */
public class GradingRequest implements Request {
    private Container container;

    public GradingRequest(Container container) {
        this.container = container;
    }

    @Override    // Command
    public void execute() {
        container.init();
    }
}
