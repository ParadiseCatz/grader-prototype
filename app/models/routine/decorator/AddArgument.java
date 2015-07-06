package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddArgument extends RoutineDecorator {
    private String argument;

    public AddArgument(Routine decoratedRoutine, String argument) {
        super(decoratedRoutine);
        this.argument = argument;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add(argument);
        return returnList;
    }
}
