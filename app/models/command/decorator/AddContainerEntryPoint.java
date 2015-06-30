package models.command.decorator;

import models.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/29/15.
 */
public class AddContainerEntryPoint extends CommandDecorator {
    private String entrypoint;

    public AddContainerEntryPoint(Command decoratedCommand, String entrypoint) {
        super(decoratedCommand);
        this.entrypoint = entrypoint;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--entrypoint=\"" + entrypoint + "\"");
        return returnList;
    }

}
