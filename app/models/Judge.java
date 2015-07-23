package models;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by anthony on 6/30/15.
 */
class Judge {
    private int id;
    private boolean timeout;
    private int exitCode;
    private File testcase;
    private File outputFile;
    private File errorFile;
    private File timeFile;
    private Constrain constrain;
    private boolean neverTimeout, neverRuntime, neverWrongAnswer;

    public Judge(int id, Constrain constrain) {
        this.id = id;
        this.constrain = constrain;
        outputFile = new File("/tmp/box/" + id + "/output.txt");
        errorFile = new File("/tmp/box/" + id + "/error.txt");
        timeFile = new File("/tmp/box/" + id + "/time.txt");
        neverTimeout = neverRuntime = neverWrongAnswer = true;
    }

    protected void setTestcase(File testcase) {
        this.testcase = testcase;
    }

    protected void setTimeoutFlag(boolean timeout) {
        this.timeout = timeout;
    }

    protected void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    boolean getCompilationPass() {
        if (errorFile.delete()) {
            try {
                errorFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return exitCode == 0;
    }

    boolean getRuntimePass() {
        if (errorFile.length() != 0)
            return neverRuntime &= false;
        if (errorFile.delete()) {
            try {
                errorFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean temporaryPass = (exitCode == 0);
        neverRuntime &= temporaryPass;
        return temporaryPass;
    }

    boolean getTimeoutPass() {
        if (timeout) {
            return neverTimeout &= false;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(timeFile)));
            StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
            double wallTime;
            try {
                wallTime = Double.parseDouble(tokenizer.nextToken());
            } catch (NumberFormatException e) {
                return neverTimeout &= false;
            }

            double userTime = Double.parseDouble(tokenizer.nextToken());
            double sysTime = Double.parseDouble(tokenizer.nextToken());

            boolean temporaryPass = ((wallTime * 1000 <= 2 * constrain.getTime()) && ((userTime + sysTime) * 1000 <= constrain.getTime()) && !timeout && exitCode != 137);
            neverTimeout &= temporaryPass;
            return temporaryPass;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            neverTimeout &= false;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            neverTimeout &= false;
            return false;
        }
    }

    boolean getWrongAnswerPass() {
        File keyFile = new File(FilenameUtils.removeExtension(testcase.getAbsolutePath()) + ".out");
        try {
            boolean temporaryPass = FileUtils.contentEquals(outputFile, keyFile);
            neverWrongAnswer &= temporaryPass;
            return temporaryPass;
        } catch (IOException e) {
            e.printStackTrace();
        }
        neverWrongAnswer &= false;
        return false;
    }

    public Verdict getOverallVerdict() {
        if (!neverRuntime) return Verdict.RE;
        if (!neverTimeout) return Verdict.TL;
        if (!neverWrongAnswer) return Verdict.WA;
        return Verdict.AC;
    }
}
