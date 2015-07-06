package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddUlimitMaxProcess extends RoutineDecorator {
    private int processNumber;

    public AddUlimitMaxProcess(Routine decoratedRoutine, int processNumber) {
        super(decoratedRoutine);
        this.processNumber = processNumber;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--ulimit");
        returnList.add("nproc=" + processNumber);
        return returnList;
    }
}
