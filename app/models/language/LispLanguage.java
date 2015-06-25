package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class LispLanguage extends SourceCode {
    public LispLanguage() {
        super(Language.LISP);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building LISP");
        return "true";
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing LISP");
        return "clisp " + super.getFileName();
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }
}
