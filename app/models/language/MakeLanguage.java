package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class MakeLanguage extends SourceCode {
    public MakeLanguage() {
        super(Language.MAKE);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building using make");
        return "make -s compile";
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing using make");
        return "make -s run";
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }

}
