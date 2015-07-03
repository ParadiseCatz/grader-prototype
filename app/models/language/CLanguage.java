package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class CLanguage extends SourceCode {
    public CLanguage() {
        super(Language.C);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building C");
        return "gcc-4.9 " + super.getFileName() + " -o " + getExecutableName();
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing C");
        return "./" + getExecutableName();
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }
}
