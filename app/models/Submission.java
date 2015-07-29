package models;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by anthony on 6/23/15.
 */
public class Submission {

    private static int counter = 0;
    public int id;
    public Date timestamp;
    private Verdict verdict = null;
    private File file = null;
    private Map<File, Verdict> testcases;

    public Submission(File file) {
        testcases = new TreeMap<>();
        this.file = file;
        this.id = counter++;
        this.timestamp = new Date();
    }

    public Map<File, Verdict> getTestcases() {
        return this.testcases;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    protected void setVerdict(Verdict verdict) {
        this.verdict = verdict;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    protected void addTestcase(File testcase) {
        this.testcases.put(testcase, null);
    }

    protected void setTestcaseVerdict(File testcase, Verdict verdict) {
        this.testcases.put(testcase, verdict);
    }

    public void unZipFile() {
    }

    protected void moveToBox(int id) {
        File destination = new File(file.getParent() + "/" + id, file.getName());
        destination.getParentFile().mkdirs();
        file.renameTo(destination);
        file = destination;
    }
}
