package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddCommand extends RoutineDecorator {
    private String command;

    public AddCommand(Routine decoratedRoutine, String command) {
        super(decoratedRoutine);
        this.command = command;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add(command);
        return returnList;
    }
}
