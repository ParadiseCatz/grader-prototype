import controllers.GradingRequest;
import controllers.Request;
import models.Grader;
import models.Submission;
import models.Verdict;
import models.ZipSubmission;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
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
    public void testJavaAC() {
        test("test/resources/AC.java", Verdict.AC);
    }

    @Test
    public void testJavaRE() {
        test("test/resources/RE.java", Verdict.RE);
    }

    @Test
    public void testJavaWA() {
        test("test/resources/WA.java", Verdict.WA);
    }

    @Test
    public void testJavaTL() {
        test("test/resources/TL.java", Verdict.TL);
    }

    @Test
    public void testCppTL() {
        test("test/resources/TL.cpp", Verdict.TL);
    }

    @Test
    public void testCppCE() {
        test("test/resources/CE.cpp", Verdict.CE);
    }

    @Test
    public void testCppACbf() {
        test("test/resources/ACbf.cpp", Verdict.TL);
    }

    @Test
    public void testZip() {
        test("test/resources/template.zip", Verdict.WA);
    }

    @Test
    public void testInvalidSubmissionType() {
        test("test/resources/invalid.docx", Verdict.CE);
    }

    @Test
    public void testMalicious() {
        test("test/resources/Connection.java", Verdict.RE);
        test("test/resources/Deadlock.java", Verdict.TL);
    }

    public void test(String filename, Verdict expectedResult) {
        running(fakeApplication(), () -> {
            File file = new File(filename);
            File temporaryStorage = new File("/tmp/box/", file.getName());
            temporaryStorage.getParentFile().mkdirs();
            try {
                org.apache.commons.io.FileUtils.copyFile(file, temporaryStorage);
            } catch (IOException e) {
                System.out.println("Cannot create temporary directory");
            }

            Submission submission;

            if (FilenameUtils.getExtension(temporaryStorage.getName()).equals("zip")) {
                submission = new ZipSubmission(temporaryStorage);
            } else {
                submission = new Submission(temporaryStorage);
            }
            Request gradingRequest = new GradingRequest(submission);
            Grader.storeAndExecute(gradingRequest);

            assertEquals(expectedResult, submission.getVerdict());

        });
    }

}
