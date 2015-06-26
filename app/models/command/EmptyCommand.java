package models.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony on 6/25/15.
 */
public class EmptyCommand implements Command {

    @Override
    public List<String> getList() {
        return new ArrayList<>();
    }
}
