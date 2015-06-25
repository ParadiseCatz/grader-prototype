package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class PythonLanguage extends SourceCode {
    public PythonLanguage() {
        super(Language.PYTHON);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building python");
        return "true";
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing python");
        return "python3 " + super.getFileName();
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }
}
