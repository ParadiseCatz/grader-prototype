import models.Verdict;
import org.junit.Test;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */

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
//        running(fakeApplication(), () -> {
//            File file = new File(filename);
//            File temporaryStorage = new File("/tmp/box/", file.getName());
//            temporaryStorage.getParentFile().mkdirs();
//            try {
//                org.apache.commons.io.FileUtils.copyFile(file, temporaryStorage);
//            } catch (IOException e) {
//                System.out.println("Cannot create temporary directory");
//            }
//
//            Submission submission;
//
//            if (FilenameUtils.getExtension(temporaryStorage.getName()).equals("zip")) {
//                submission = new ZipSubmission(temporaryStorage);
//            } else {
//                submission = new Submission(temporaryStorage);
//            }
//            Request gradingRequest = new GradingRequest(submission);
//            Grader.storeAndExecute(gradingRequest);
//
//            assertEquals(expectedResult, submission.getVerdict());
//
//        });
    }

}
