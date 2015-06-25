package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class JavaLanguage extends SourceCode {
    public JavaLanguage() {
        super(Language.JAVA);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building java");
        return "javac " + super.getFileName();
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing java");
        return "java " + getExecutableName();
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }
}
