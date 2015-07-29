package models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

        doThrow(InternalError.class).when(mockedSubmission).moveToBox(anyInt());
        exception.expect(InternalError.class);
        spiedContainer.init();
        reset(mockedSubmission);
    }

    @Test
    public void testUnzipInInit() throws Exception {
        System.out.println("Test Unzipping in Container.init()");
        Submission mockedSubmission = mock(Submission.class);
        Constrain mockedConstrain = mock(Constrain.class);
        Container spiedContainer = spy(new Container(mockedSubmission, mockedConstrain));

        doNothing().when(mockedSubmission).moveToBox(anyInt());
        doThrow(InternalError.class).when(mockedSubmission).unZipFile();
        exception.expect(InternalError.class);
        spiedContainer.init();
        reset(mockedSubmission);
    }

    @Test
    public void testRun() throws Exception {

    }

    @Test
    public void testCompile() throws Exception {

    }

    @Test
    public void testExecute() throws Exception {

    }

    @Test
    public void testAttempt() throws Exception {

    }

    @Test
    public void testAddContainerParameters() throws Exception {

    }

    @Test
    public void testAddContainerUlimit() throws Exception {

    }
}