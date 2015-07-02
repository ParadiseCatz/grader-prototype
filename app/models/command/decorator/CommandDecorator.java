package models.command.decorator;

import models.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
abstract public class CommandDecorator implements Command {
    private Command decoratedCommand;

    CommandDecorator(Command decoratedCommand) {
        this.decoratedCommand = decoratedCommand;
    }

    public List<String> getList() {
        return decoratedCommand.getList();
    }
}
