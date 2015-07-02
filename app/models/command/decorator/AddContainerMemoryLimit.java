package models.command.decorator;

import models.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerMemoryLimit extends CommandDecorator {
    private Integer limit;

    public AddContainerMemoryLimit(Command decoratedCommand, Integer limit) {
        super(decoratedCommand);
        this.limit = limit;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--memory=" + limit);
        return returnList;
    }
}
