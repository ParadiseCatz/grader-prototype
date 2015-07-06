package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerMemoryLimit extends RoutineDecorator {
    private Integer limit;

    public AddContainerMemoryLimit(Routine decoratedRoutine, Integer limit) {
        super(decoratedRoutine);
        this.limit = limit;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--memory=" + limit);
        return returnList;
    }
}
