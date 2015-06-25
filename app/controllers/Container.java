package controllers;

import controllers.command.Command;
import controllers.command.EmptyCommand;
import controllers.command.decorator.*;
import models.SourceCode;
import models.SourceCodeFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Created by anthony on 6/24/15.
 */
public class Container {
    private static int counter = 0;
    private final int id;
    private File submission;

    public Container(File submission) {
        this.submission = submission;
        this.id = counter++;
    }

    public void init() {
        System.out.println("Creating Box-" + id);

        File destination = new File(submission.getParent() + "/" + id, submission.getName());
        if (!destination.getParentFile().mkdirs()) {
        }
        submission.renameTo(destination);
        submission = destination;
        run();
    }

    public void run() {
        System.out.println("Running Box-" + id);
        SourceCode sourceCode = SourceCodeFactory.buildSourceCode(submission.getName());

        attempt((t) -> compile(t), sourceCode);
        attempt((t) -> execute(t), sourceCode);
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

        command = new AddCommand(command, "/bin/sh");
        command = new AddArgument(command, "-c");

        command = new AddCommand(command, sourceCode.getExecutionCommand());

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
            future.get(3, TimeUnit.SECONDS);
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            future.cancel(true);
            dockerTermination = new AddArgument(dockerTermination, "-f");
            System.out.println(id + " Terminated!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Exit Code: " + getExitCode());
        dockerTermination = new AddArgument(dockerTermination, "box" + id);

        try {
            Process p = new ProcessBuilder(dockerTermination.getList()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
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
