package controllers.command.decorator;

import controllers.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
abstract public class CommandDecorator implements Command {
    protected Command decoratedCommand;

    public CommandDecorator(Command decoratedCommand) {
        this.decoratedCommand = decoratedCommand;
    }

    public List<String> getList() {
        return decoratedCommand.getList();
    }
}
