package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddUlimitMaxDiskUsage extends RoutineDecorator {
    private int fileSize;

    public AddUlimitMaxDiskUsage(Routine decoratedRoutine, int fileSize) {
        super(decoratedRoutine);
        this.fileSize = fileSize;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--ulimit");
        returnList.add("fsize=" + fileSize);
        return returnList;
    }
}
