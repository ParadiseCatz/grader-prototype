package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerName extends RoutineDecorator {
    private String name;

    public AddContainerName(Routine decoratedRoutine, String name) {
        super(decoratedRoutine);
        this.name = name;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--name=" + name);
        return returnList;
    }
}
