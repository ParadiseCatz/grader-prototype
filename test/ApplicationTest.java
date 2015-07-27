import org.junit.Test;
import play.mvc.Result;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void testTemplate() {
        running(fakeApplication(), () -> {
            Result result = route(controllers.routes.Application.template());
            assertEquals(OK, result.status());
            assertEquals("application/zip", result.contentType());
        });
    }

    @Test
    public void testMockito() {
        List mockedList;
        mockedList = mock(List.class);
    }
}
