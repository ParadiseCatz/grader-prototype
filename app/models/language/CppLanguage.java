package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class CppLanguage extends SourceCode {
    public CppLanguage() {
        super(Language.CPP);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building Cpp");
        return "g++ " + super.getFileName() + " -o " + getExecutableName();
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing Cpp");
        return "./" + getExecutableName();
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }
}
