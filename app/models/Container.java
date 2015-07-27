package models;

import models.routine.EmptyRoutine;
import models.routine.Routine;
import models.routine.decorator.*;
import org.apache.commons.io.FilenameUtils;
import play.Play;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Created by anthony on 6/24/15.
 */
public class Container {
    private static int counter = 0;
    private final int id;
    private Submission submission;
    private BufferedReader testcaseThrower = null;
    private Judge judge;
    private Constrain constrain;

    public Container(Submission submission) {
        this.submission = submission;
        this.id = counter++;
        this.init();
    }

    public int getId() {
        return id;
    }

    void init() {
        System.out.println("Creating Box-" + id);

        constrain = new Constrain(Play.application().configuration().getInt("problem.timelimit"), Play.application().configuration().getInt("problem.memorylimit"));
        judge = new Judge(id, constrain);

        File destination = new File(submission.getFile().getParent() + "/" + id, submission.getFile().getName());
        destination.getParentFile().mkdirs();
        submission.getFile().renameTo(destination);
        submission.setFile(destination);
        submission.unZipFile();
        run();
    }

    void run() {
        System.out.println("Running Box-" + id);
        SourceCode sourceCode;
        try {
            sourceCode = SourceCodeFactory.buildSourceCode(submission.getFile().getName());
        } catch (IllegalArgumentException e) {
            submission.setVerdict(Verdict.CE);
            return;
        }

        attempt(this::compile, sourceCode);
        if (!judge.getCompilationPass()) {
            submission.setVerdict(Verdict.CE);
            return;
        }
        System.out.println();

        File testcaseDirectory = new File("testcase/");
        File[] testcaseFiles = testcaseDirectory.listFiles();
        assert testcaseFiles != null;
        Arrays.sort(testcaseFiles);
        for (File testcase : testcaseFiles) {
            if (FilenameUtils.getExtension(testcase.getName()).equals("in")) {
                try {
                    System.out.println("Testcase: " + testcase.getName());
                    testcaseThrower = new BufferedReader(new InputStreamReader(new FileInputStream(testcase)));
                    submission.addTestcase(testcase);
                    judge.setTestcase(testcase);
                    attempt(this::execute, sourceCode);
                    testcaseThrower.close();
                } catch (IOException e) {
                    judge.setTestcase(null);
                    System.out.println("!testcase not found!");
                }
                System.out.println();

                if (!judge.getTimeoutPass()) {
                    submission.setTestcaseVerdict(testcase, Verdict.TL);
                    continue;
                }
                if (!judge.getRuntimePass()) {
                    submission.setTestcaseVerdict(testcase, Verdict.RE);
                    continue;
                }
                if (!judge.getWrongAnswerPass()) {
                    submission.setTestcaseVerdict(testcase, Verdict.WA);
                    continue;
                }
                submission.setTestcaseVerdict(testcase, Verdict.AC);
            }
        }
        submission.setVerdict(judge.getOverallVerdict());
        System.out.println("Grading Finished");
    }

    void compile(SourceCode sourceCode) {
        Routine routine = new EmptyRoutine();
        routine = new AddCommand(routine, "docker");
        routine = new AddArgument(routine, "run");
        routine = addContainerParameters(routine);

        routine = new AddCommand(routine, "/bin/sh");
        routine = new AddArgument(routine, "-c");

        routine = new AddCommand(routine, sourceCode.getCompilationCommand());

        ProcessBuilder pb = new ProcessBuilder(routine.getList());
        pb.redirectErrorStream(true);

        Process proc;
        try {
            proc = pb.start();
            proc.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = reader.readLine()) != null)
                System.out.println(s);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Compiling Terminated!");
        }
    }

    void execute(SourceCode sourceCode) {
        Routine routine = new EmptyRoutine();

        routine = new AddCommand(routine, "docker");
        routine = new AddArgument(routine, "run");
        routine = new AddArgument(routine, "-i");
        routine = addContainerParameters(routine);

        routine = new AddCommand(routine, "/bin/bash");
        routine = new AddArgument(routine, "-c");

        routine = new AddCommand(routine, "TIMEFORMAT=%R' '%U' '%S && { time " + sourceCode.getExecutionCommand() + ">output.txt 2>err.txt; } &> time.txt");

        ProcessBuilder pb = new ProcessBuilder(routine.getList());
        pb.redirectErrorStream(true);

        Process proc;
        try {
            proc = pb.start();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = testcaseThrower.readLine()) != null) {
                writer.write(s + "\n");
            }
            writer.close();
            while ((s = reader.readLine()) != null)
                System.out.println(s);
            reader.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Execution Terminated!");
        }
    }


    void attempt(Consumer<SourceCode> command, SourceCode sourceCode) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            command.accept(sourceCode);
            return null;
        });

        Routine dockerTerminationRoutine = new EmptyRoutine();
        dockerTerminationRoutine = new AddCommand(dockerTerminationRoutine, "docker");
        dockerTerminationRoutine = new AddArgument(dockerTerminationRoutine, "rm");
        boolean timeout = false;
        try {
            System.out.println("Started..");
            future.get(2 * constrain.getTime(), TimeUnit.MILLISECONDS);
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            future.cancel(true);
            dockerTerminationRoutine = new AddArgument(dockerTerminationRoutine, "-f");
            System.out.println("Box-" + id + " Terminated!");
            timeout = true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        judge.setTimeoutFlag(timeout);
        executor.shutdownNow();
        try {
            executor.awaitTermination(2 * constrain.getTime(), TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int temporaryExitCode = getExitCode();
        System.out.println("Exit Code: " + temporaryExitCode);
        dockerTerminationRoutine = new AddArgument(dockerTerminationRoutine, "box" + id);

        try {
            Process p = new ProcessBuilder(dockerTerminationRoutine.getList()).start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        judge.setExitCode(temporaryExitCode);
    }

    private int getExitCode() {
        String output = "";

        Routine routine = new EmptyRoutine();

        routine = new AddCommand(routine, "docker");
        routine = new AddArgument(routine, "inspect");
        routine = new AddArgument(routine, "-f");
        routine = new AddArgument(routine, "{{.State.ExitCode}}");
        routine = new AddArgument(routine, "box" + id);
        try {
            Process proc = new ProcessBuilder(routine.getList()).start();
            proc.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = reader.readLine()) != null)
                output += s;
            reader.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(output.trim());
    }

    Routine addContainerParameters(Routine routine) {
        routine = addContainerUlimit(routine);
        routine = new AddContainerNetworkMode(routine, "none");
        routine = new AddContainerName(routine, "box" + id);
        routine = new AddContainerVolume(routine, submission.getFile().getParent());
        routine = new AddContainerMemoryLimit(routine, constrain.getMemory());
        routine = new AddContainerImage(routine, Play.application().configuration().getString("docker.image.name"));
        return routine;
    }

    Routine addContainerUlimit(Routine routine) {
        routine = new AddUlimitMaxProcess(routine, 100);
        routine = new AddUlimitMaxOpenFiles(routine, 50);
        routine = new AddUlimitMaxCpuTime(routine, (constrain.getTime() + 1000 - 1) / 1000);
        routine = new AddUlimitMaxResources(routine, constrain.getMemory());
        routine = new AddUlimitMaxDiskUsage(routine, 1024 * 512);
        return routine;
    }
}
