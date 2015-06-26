package models.command.decorator;

import models.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddArgument extends CommandDecorator {
    private String argument;

    public AddArgument(Command decoratedCommand, String argument) {
        super(decoratedCommand);
        this.argument = argument;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add(argument);
        return returnList;
    }
}
