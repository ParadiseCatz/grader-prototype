package models;

import models.routine.Routine;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class ContainerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testMovetoBoxInInit() throws Exception {
        System.out.println("Test moving to box in Container.init()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));

        doThrow(TestingOKException.class).when(mockedSubmission).moveToBox(anyInt());

        exception.expect(TestingOKException.class);
        spiedContainer.init();
    }

    @Test
    public void testUnzipInInit() throws Exception {
        System.out.println("Test Unzipping in Container.init()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));

        doNothing().when(mockedSubmission).moveToBox(anyInt());
        doThrow(TestingOKException.class).when(mockedSubmission).unZipFile();

        exception.expect(TestingOKException.class);
        spiedContainer.init();
    }

    @Test
    public void testInvalidTypeInRun() throws Exception {
        System.out.println("Test Invalid file type in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));

        when(mockedSubmission.getFile()).thenReturn(new File("invalid.docx"));
        doThrow(TestingOKException.class).when(mockedSubmission).setVerdict(Verdict.CE);

        exception.expect(TestingOKException.class);
        spiedContainer.run();
    }

    @Test
    public void testCompileInRun() throws Exception {
        System.out.println("Test Compile being called in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));

        Consumer<SourceCode> expectedCommand = spiedContainer::compile;

        when(mockedSubmission.getFile()).thenReturn(new File("AC.cpp"));
        doThrow(TestingOKException.class).doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("compile"));

        exception.expect(TestingOKException.class);
        spiedContainer.run();
    }

    @Test
    public void testCheckCompilePassInRun() throws Exception {
        System.out.println("Test Check Compiling Pass in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);

        when(mockedSubmission.getFile()).thenReturn(new File("AC.cpp"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("compile"));
        when(mockedJudge.getCompilationPass()).thenReturn(false);
        doThrow(TestingOKException.class).when(mockedSubmission).setVerdict(Verdict.CE);

        exception.expect(TestingOKException.class);
        spiedContainer.run();
    }

    @Test
    public void testAddTestcaseInRun() throws Exception {
        System.out.println("Test Add Testcase in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);

        when(mockedSubmission.getFile()).thenReturn(new File("AC.cpp"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("compile"));
        when(mockedJudge.getCompilationPass()).thenReturn(true);
        doThrow(TestingOKException.class).when(mockedSubmission).addTestcase(any(File.class));

        exception.expect(TestingOKException.class);
        spiedContainer.run();
    }

    @Test
    public void testSetTestcaseForJudgeInRun() throws Exception {
        System.out.println("Test Set Testcase for Judge in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);

        when(mockedSubmission.getFile()).thenReturn(new File("AC.cpp"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("compile"));
        when(mockedJudge.getCompilationPass()).thenReturn(true);
        doThrow(TestingOKException.class).when(mockedJudge).setTestcase(any(File.class));

        exception.expect(TestingOKException.class);
        spiedContainer.run();
    }

    @Test
    public void testExecuteInRun() throws Exception {
        System.out.println("Test Execute being called in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);

        when(mockedSubmission.getFile()).thenReturn(new File("AC.cpp"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("compile"));
        when(mockedJudge.getCompilationPass()).thenReturn(true);
        doThrow(TestingOKException.class).when(spiedContainer).attempt(any(), any(SourceCode.class), eq("execute"));

        exception.expect(TestingOKException.class);
        spiedContainer.run();
    }

    @Test
    public void testJudgingOrderInRun() throws Exception {
        System.out.println("Test Judging Order in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);

        when(mockedSubmission.getFile()).thenReturn(new File("AC.cpp"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("compile"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("execute"));

        when(mockedJudge.getCompilationPass()).thenReturn(true);
        when(mockedJudge.getTimeoutPass()).thenReturn(true).thenReturn(false);
        when(mockedJudge.getRuntimePass()).thenReturn(true);
        when(mockedJudge.getWrongAnswerPass()).thenReturn(true);
        InOrder inOrder = inOrder(mockedJudge);

        spiedContainer.run();

        inOrder.verify(mockedJudge, times(1)).getCompilationPass();
        inOrder.verify(mockedJudge).getTimeoutPass();
        inOrder.verify(mockedJudge, times(1)).getRuntimePass();
        inOrder.verify(mockedJudge, times(1)).getWrongAnswerPass();

    }

    @Test
    public void testSettingTimeoutFlagInAttempt() throws Exception {
        System.out.println("Test Setting timeout flag in Container.attempt()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);

        @SuppressWarnings("unchecked")
        Consumer<SourceCode> mockedCommand = (Consumer<SourceCode>) mock(Consumer.class);
        SourceCode mockedSourceCode = mock(SourceCode.class);
        doThrow(TestingFAILException.class).when(mockedCommand).accept(mockedSourceCode);
        doThrow(TestingOKException.class).doNothing().when(mockedJudge).setTimeoutFlag(anyBoolean());
        doThrow(ExecutionException.class).when(mockedConstrain).getTime();

        exception.expect(TestingOKException.class);
        spiedContainer.attempt(eq(mockedCommand), eq(mockedSourceCode), anyString());
    }


    @Test
    public void testJudgeOverallVerdictInRun() throws Exception {
        System.out.println("Test Judge Overall Verdict in Container.run()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);

        when(mockedSubmission.getFile()).thenReturn(new File("AC.cpp"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("compile"));
        doNothing().when(spiedContainer).attempt(any(), any(SourceCode.class), eq("execute"));
        when(mockedJudge.getCompilationPass()).thenReturn(true);
        when(mockedJudge.getTimeoutPass()).thenReturn(false);
        doThrow(TestingOKException.class).when(mockedJudge).getOverallVerdict();

        exception.expect(TestingOKException.class);
        spiedContainer.run();
    }

    @Test
    public void testCompileCommandInCompile() throws Exception {
        System.out.println("Test Compile Command being called in Container.compile()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);
        SourceCode mockedSourceCode = mock(SourceCode.class);
        Routine mockedRoutine = mock(Routine.class);

        doReturn(mockedRoutine).when(spiedContainer).addContainerParameters(any());
        doThrow(TestingOKException.class).when(mockedSourceCode).getCompilationCommand();

        exception.expect(TestingOKException.class);
        spiedContainer.compile(mockedSourceCode);
    }

    @Test
    public void testExecuteCommandInExecute() throws Exception {
        System.out.println("Test Execute Command being called in Container.execute()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));
        Judge mockedJudge = mock(Judge.class);
        spiedContainer.setJudge(mockedJudge);
        SourceCode mockedSourceCode = mock(SourceCode.class);
        Routine mockedRoutine = mock(Routine.class);

        doReturn(mockedRoutine).when(spiedContainer).addContainerParameters(any());
        doThrow(TestingOKException.class).when(mockedSourceCode).getExecutionCommand();

        exception.expect(TestingOKException.class);
        spiedContainer.execute(mockedSourceCode);
    }

    @Test
    public void testAddContainerParameters() throws Exception {

    }

    @Test
    public void testAddContainerUlimit() throws Exception {

    }
}

class TestingOKException extends Exception {
    //Parameterless Constructor
    public TestingOKException() {
    }

    //Constructor that accepts a message
    public TestingOKException(String message) {
        super(message);
    }
}

class TestingFAILException extends Exception {
    //Parameterless Constructor
    public TestingFAILException() {
    }

    //Constructor that accepts a message
    public TestingFAILException(String message) {
        super(message);
    }
}