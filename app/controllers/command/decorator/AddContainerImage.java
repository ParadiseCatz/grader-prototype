package controllers.command.decorator;

import controllers.command.Command;

import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class AddContainerImage extends CommandDecorator {
    private String imageName;

    public AddContainerImage(Command decoratedCommand, String imageName) {
        super(decoratedCommand);
        this.imageName = imageName;
    }

    @Override
    public List<String> getList() {
        List<String> returnList = super.getList();
        returnList.add(imageName);
        return returnList;
    }
}
