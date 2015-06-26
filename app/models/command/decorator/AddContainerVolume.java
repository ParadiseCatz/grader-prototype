package models.command.decorator;

import models.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerVolume extends CommandDecorator {
    private String path;

    public AddContainerVolume(Command decoratedCommand, String path) {
        super(decoratedCommand);
        this.path = path;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--volume=" + path + ":/box");
        return returnList;
    }
}
