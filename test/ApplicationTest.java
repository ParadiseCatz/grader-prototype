import controllers.GradingRequest;
import controllers.Request;
import models.Grader;
import models.Submission;
import models.Verdict;
import models.ZipSubmission;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    Map<String, String> map = new HashMap<>();

    @Before
    public void init() {
        map.put("docker.image.name", "paradisecatz/grader-container");
        map.put("problem.timelimit", "10000");
        map.put("problem.memorylimit", "268435456");
        map.put("mandatory.file.list", "[\"makefile\", \"main.cpp\"]");
    }

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

    public void test(String filename, Verdict result) {
        running(fakeApplication(map), () -> {
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

            assertEquals(result, submission.getVerdict());

        });
    }

}
