package models;

/**
 * Created by anthony on 6/23/15.
 */
public abstract class SourceCode {

    private Language language = null;

    private String fileName = null;

    public SourceCode(Language language) {
        this.language = language;
    }

    public abstract String getCompilationCommand();

    public abstract String getExecutionCommand();

    public abstract String getExecutableName();

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
