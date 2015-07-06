package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddUlimitMaxResources extends RoutineDecorator {
    private int resources;

    public AddUlimitMaxResources(Routine decoratedRoutine, int resources) {
        super(decoratedRoutine);
        this.resources = resources;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--ulimit");
        returnList.add("rss=" + resources);
        return returnList;
    }
}
