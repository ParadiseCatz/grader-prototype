package models;

import models.command.Command;
import models.command.EmptyCommand;
import models.command.decorator.*;
import org.apache.commons.io.FilenameUtils;

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

        constrain = new Constrain(200000, 268435456);
        judge = new Judge(id, constrain);

        File destination = new File(submission.getFile().getParent() + "/" + id, submission.getFile().getName());
        destination.getParentFile().mkdirs();
        submission.getFile().renameTo(destination);
        submission.setFile(destination);
        run();
    }

    void run() {
        System.out.println("Running Box-" + id);
        SourceCode sourceCode = SourceCodeFactory.buildSourceCode(submission.getFile().getName());

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

                if (!judge.getRuntimePass()) {
                    submission.setTestcaseVerdict(testcase, Verdict.RE);
                    continue;
                }
                if (!judge.getTimeoutPass()) {
                    submission.setTestcaseVerdict(testcase, Verdict.TL);
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
        Command command = new EmptyCommand();
        command = new AddCommand(command, "docker");
        command = new AddArgument(command, "run");
        command = new AddContainerNetworkMode(command, "none");
        command = new AddContainerName(command, "box" + id);
        command = new AddContainerVolume(command, submission.getFile().getParent());
        command = new AddContainerMemoryLimit(command, constrain.getMemory());
        command = new AddContainerImage(command, "test2");

        command = new AddCommand(command, "/bin/sh");
        command = new AddArgument(command, "-c");

        command = new AddCommand(command, sourceCode.getCompilationCommand());

        ProcessBuilder pb = new ProcessBuilder(command.getList());
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

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void execute(SourceCode sourceCode) {
        Command command = new EmptyCommand();

        command = new AddCommand(command, "docker");
        command = new AddArgument(command, "run");
        command = new AddArgument(command, "-i");
        command = new AddContainerNetworkMode(command, "none");
        command = new AddContainerName(command, "box" + id);
        command = new AddContainerVolume(command, submission.getFile().getParent());
        command = new AddContainerMemoryLimit(command, constrain.getMemory());
        command = new AddContainerImage(command, "test2");

        command = new AddCommand(command, "/bin/bash");
        command = new AddArgument(command, "-c");

        command = new AddCommand(command, "TIMEFORMAT=%R' '%U' '%S && { time " + sourceCode.getExecutionCommand() + ">output.txt 2>err.txt; } &> time.txt");

        ProcessBuilder pb = new ProcessBuilder(command.getList());
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
            e.printStackTrace();
        }
    }


    void attempt(Consumer<SourceCode> command, SourceCode sourceCode) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            command.accept(sourceCode);
            return null;
        });

        Command dockerTerminationCommand = new EmptyCommand();
        dockerTerminationCommand = new AddCommand(dockerTerminationCommand, "docker");
        dockerTerminationCommand = new AddArgument(dockerTerminationCommand, "rm");
        boolean timeout = false;
        try {
            System.out.println("Started..");
            future.get(2 * constrain.getTime(), TimeUnit.MILLISECONDS);
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            future.cancel(true);
            dockerTerminationCommand = new AddArgument(dockerTerminationCommand, "-f");
            System.out.println("Box-" + id + " Terminated!");
            timeout = true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        judge.setTimeoutFlag(timeout);
        executor.shutdownNow();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int temporaryExitCode = getExitCode();
        System.out.println("Exit Code: " + temporaryExitCode);
        dockerTerminationCommand = new AddArgument(dockerTerminationCommand, "box" + id);

        try {
            Process p = new ProcessBuilder(dockerTerminationCommand.getList()).start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        judge.setExitCode(temporaryExitCode);
    }

    private int getExitCode() {
        String output = "";

        Command command = new EmptyCommand();

        command = new AddCommand(command, "docker");
        command = new AddArgument(command, "inspect");
        command = new AddArgument(command, "-f");
        command = new AddArgument(command, "{{.State.ExitCode}}");
        command = new AddArgument(command, "box" + id);
        try {
            Process proc = new ProcessBuilder(command.getList()).start();
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


}
