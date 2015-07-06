package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
abstract public class RoutineDecorator implements Routine {
    private Routine decoratedRoutine;

    RoutineDecorator(Routine decoratedRoutine) {
        this.decoratedRoutine = decoratedRoutine;
    }

    public List<String> getList() {
        return decoratedRoutine.getList();
    }
}
