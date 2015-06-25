package controllers.command.decorator;

import controllers.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerName extends CommandDecorator {
    private String name;

    public AddContainerName(Command decoratedCommand, String name) {
        super(decoratedCommand);
        this.name = name;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--name=" + name);
        return returnList;
    }
}
