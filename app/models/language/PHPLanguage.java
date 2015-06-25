package models.language;

import models.Language;
import models.SourceCode;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class PHPLanguage extends SourceCode {
    public PHPLanguage() {
        super(Language.PHP);
    }

    @Override
    public String getCompilationCommand() {
        System.out.println("Building PHP");
        return "true";
    }

    @Override
    public String getExecutionCommand() {
        System.out.println("Executing PHP");
        return "php " + super.getFileName();
    }

    @Override
    public String getExecutableName() {
        return FilenameUtils.getBaseName(super.getFileName());
    }
}
