package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddUlimitMaxOpenFiles extends RoutineDecorator {
    private int fileNumber;

    public AddUlimitMaxOpenFiles(Routine decoratedRoutine, int fileNumber) {
        super(decoratedRoutine);
        this.fileNumber = fileNumber;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--ulimit");
        returnList.add("nofile=" + fileNumber);
        return returnList;
    }
}
