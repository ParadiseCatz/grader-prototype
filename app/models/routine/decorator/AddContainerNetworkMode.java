package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerNetworkMode extends RoutineDecorator {
    private String networkMode;

    public AddContainerNetworkMode(Routine decoratedRoutine, String networkMode) {
        super(decoratedRoutine);
        this.networkMode = networkMode;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--net=" + networkMode);
        return returnList;
    }
}
