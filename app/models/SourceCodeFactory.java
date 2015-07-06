package models;

import models.language.*;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by anthony on 6/23/15.
 */
public class SourceCodeFactory {
    public static SourceCode buildSourceCode(String fileName) {
        SourceCode sourceCode;
        String ext = FilenameUtils.getExtension(fileName);
        switch (ext) {
            case "cpp":
                sourceCode = new CppLanguage();
                break;

            case "c":
                sourceCode = new CLanguage();
                break;

            case "pas":
                sourceCode = new PascalLanguage();
                break;

            case "py":
                sourceCode = new PythonLanguage();
                break;

            case "java":
                sourceCode = new JavaLanguage();
                break;

            case "php":
                sourceCode = new PHPLanguage();
                break;

            case "lisp":
                sourceCode = new LispLanguage();
                break;

            case "zip":
                sourceCode = new MakeLanguage();
                break;
            default:
                throw new IllegalArgumentException("Invalid language!");
        }
        sourceCode.setFileName(fileName);
        return sourceCode;
    }
}
