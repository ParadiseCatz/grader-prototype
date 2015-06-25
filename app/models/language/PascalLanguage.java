package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class PascalLanguage extends SourceCode {
    public PascalLanguage() {
        super(Language.PASCAL);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building Pascal");
        return "fpc " + super.getFileName();
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing Pascal");
        return "./" + getExecutableName();
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }
}
