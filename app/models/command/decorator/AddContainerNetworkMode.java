package models.command.decorator;

import models.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerNetworkMode extends CommandDecorator {
    private String networkMode;

    public AddContainerNetworkMode(Command decoratedCommand, String networkMode) {
        super(decoratedCommand);
        this.networkMode = networkMode;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--net=" + networkMode);
        return returnList;
    }
}
