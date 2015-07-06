package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddUlimitMaxCpuTime extends RoutineDecorator {
    private int CpuTime;

    public AddUlimitMaxCpuTime(Routine decoratedRoutine, int CpuTime) {
        super(decoratedRoutine);
        this.CpuTime = CpuTime;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add("--ulimit");
        returnList.add("cpu=" + CpuTime);
        return returnList;
    }
}
