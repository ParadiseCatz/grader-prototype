package models;

import models.language.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SourceCodeFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBuildSourceCode() throws Exception {
        assertEquals(CppLanguage.class, SourceCodeFactory.buildSourceCode("ASD.cpp").getClass());
        assertEquals(CLanguage.class, SourceCodeFactory.buildSourceCode("ASD.c").getClass());
        assertEquals(PascalLanguage.class, SourceCodeFactory.buildSourceCode("ASD.pas").getClass());
        assertEquals(JavaLanguage.class, SourceCodeFactory.buildSourceCode("ASD.java").getClass());
        assertEquals(LispLanguage.class, SourceCodeFactory.buildSourceCode("ASD.lisp").getClass());
        assertEquals(PHPLanguage.class, SourceCodeFactory.buildSourceCode("ASD.php").getClass());
        assertEquals(PythonLanguage.class, SourceCodeFactory.buildSourceCode("ASD.py").getClass());
        assertEquals(MakeLanguage.class, SourceCodeFactory.buildSourceCode("ASD.zip").getClass());

        // Supposed to be failed
        SourceCodeFactory.buildSourceCode("ASD.docx");
    }
}