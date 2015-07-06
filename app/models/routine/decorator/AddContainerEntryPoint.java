package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/29/15.
 */
public class AddContainerEntryPoint extends RoutineDecorator {
    private String entrypoint;

    public AddContainerEntryPoint(Routine decoratedRoutine, String entrypoint) {
        super(decoratedRoutine);
        this.entrypoint = entrypoint;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--entrypoint=\"" + entrypoint + "\"");
        return returnList;
    }

}
