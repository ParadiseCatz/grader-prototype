package controllers.command.decorator;

import controllers.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddCommand extends CommandDecorator {
    private String command;

    public AddCommand(Command decoratedCommand, String command) {
        super(decoratedCommand);
        this.command = command;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add(command);
        return returnList;
    }
}
