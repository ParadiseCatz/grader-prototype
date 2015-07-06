package models.routine.decorator;

import models.routine.Routine;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerImage extends RoutineDecorator {
    private String imageName;

    public AddContainerImage(Routine decoratedRoutine, String imageName) {
        super(decoratedRoutine);
        this.imageName = imageName;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add(imageName);
        return returnList;
    }
}
