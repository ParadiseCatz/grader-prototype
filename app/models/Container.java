package models;

import models.command.Command;
import models.command.EmptyCommand;
import models.command.decorator.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Created by anthony on 6/24/15.
 */
public class Container {
    private static int counter = 0;
    private final int id;
    private File submission;
    private volatile String exitCode = null;

    public Container(File submission) {
        this.submission = submission;
        this.id = counter++;
    }

    public int getId() {
        return id;
    }

    public void init() {
        System.out.println("Creating Box-" + id);

        File destination = new File(submission.getParent() + "/" + id, submission.getName());
        destination.getParentFile().mkdirs();
        submission.renameTo(destination);
        submission = destination;
        run();
    }

    public void run() {
        System.out.println("Running Box-" + id);
        long startTime, endTime, duration, cpuTime;
        SourceCode sourceCode = SourceCodeFactory.buildSourceCode(submission.getName());

        startTime = System.nanoTime();
        ThreadMXBean compileThread = ManagementFactory.getThreadMXBean();
        attempt(this::compile, sourceCode);
        endTime = System.nanoTime();

        duration = (endTime - startTime);
        System.out.println("wall time: " + duration / 1000000 + "ms");
        cpuTime = compileThread.getCurrentThreadCpuTime();
        System.out.println("cpu time: " + cpuTime / 1000000 + "ms" + "\n");

        startTime = System.nanoTime();
        ThreadMXBean executeThread = ManagementFactory.getThreadMXBean();
        attempt(this::execute, sourceCode);
        endTime = System.nanoTime();

        duration = (endTime - startTime);
        System.out.println("wall time: " + duration / 1000000 + "ms");
        cpuTime = executeThread.getCurrentThreadCpuTime();
        System.out.println("cpu time: " + cpuTime / 1000000 + "ms" + "\n");

    }

    public void compile(SourceCode sourceCode) {
        Command command = new EmptyCommand();
        command = new AddCommand(command, "docker");
        command = new AddArgument(command, "run");
        command = new AddContainerNetworkMode(command, "none");
        command = new AddContainerName(command, "box" + id);
        command = new AddContainerVolume(command, submission.getParent());
        command = new AddContainerMemoryLimit(command, "100M");
        command = new AddContainerImage(command, "test2");

        command = new AddCommand(command, "/bin/sh");
        command = new AddArgument(command, "-c");

        command = new AddCommand(command, sourceCode.getCompilationCommand());

        ProcessBuilder pb = new ProcessBuilder(command.getList());
        pb.redirectErrorStream(true);

        try {
            Process proc = pb.start();
            Reader reader = new InputStreamReader(proc.getInputStream());
            int ch;
            while ((ch = reader.read()) != -1)
                System.out.print((char) ch);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(pb.command());
    }

    public void execute(SourceCode sourceCode) {
        Command command = new EmptyCommand();

        command = new AddCommand(command, "docker");
        command = new AddArgument(command, "run");
        command = new AddContainerNetworkMode(command, "none");
        command = new AddContainerName(command, "box" + id);
        command = new AddContainerVolume(command, submission.getParent());
        command = new AddContainerMemoryLimit(command, "100M");
        command = new AddContainerImage(command, "test2");

        command = new AddCommand(command, "/bin/bash");
        command = new AddArgument(command, "-c");

        command = new AddCommand(command, "(time " + sourceCode.getExecutionCommand() + ") &> output.txt");

        ProcessBuilder pb = new ProcessBuilder(command.getList());
        pb.redirectErrorStream(true);

        try {
            Process proc = pb.start();
            Reader reader = new InputStreamReader(proc.getInputStream());
            int ch;
            while ((ch = reader.read()) != -1)
                System.out.print((char) ch);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void attempt(Consumer<SourceCode> command, SourceCode sourceCode) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            command.accept(sourceCode);
            return null;
        });

        Command dockerTermination = new EmptyCommand();
        dockerTermination = new AddCommand(dockerTermination, "docker");
        dockerTermination = new AddArgument(dockerTermination, "rm");
        try {
            System.out.println("Started..");
//            System.out.println(future.get(10, TimeUnit.SECONDS));
            future.get(5, TimeUnit.SECONDS);
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            future.cancel(true);
            dockerTermination = new AddArgument(dockerTermination, "-f");
            System.out.println("Box-" + id + " Terminated!");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String temporaryExitCode = getExitCode();
        System.out.print("Exit Code: " + temporaryExitCode);
        dockerTermination = new AddArgument(dockerTermination, "box" + id);

        try {
            Process p = new ProcessBuilder(dockerTermination.getList()).start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
        exitCode = temporaryExitCode;
    }

    private String getExitCode() {
        String output = "";

        Command command = new EmptyCommand();

        command = new AddCommand(command, "docker");
        command = new AddArgument(command, "inspect");
        command = new AddArgument(command, "-f");
        command = new AddArgument(command, "{{.State.ExitCode}}");
        command = new AddArgument(command, "box" + id);
        try {
            Process proc = new ProcessBuilder(command.getList()).start();
            Reader reader = new InputStreamReader(proc.getInputStream());
            int ch;
            while ((ch = reader.read()) != -1)
                output += (char) ch;
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }


}
