package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerVolume extends RoutineDecorator {
    private String path;

    public AddContainerVolume(Routine decoratedRoutine, String path) {
        super(decoratedRoutine);
        this.path = path;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--volume=" + path + ":/box");
        return returnList;
    }
}
