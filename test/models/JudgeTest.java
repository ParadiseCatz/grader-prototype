package models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class JudgeTest {

    @Test
    public void testGetTimeoutPass() throws Exception {
        Constrain mockedConstrain = mock(Constrain.class);
        Judge spiedJudge = spy(new Judge(0, mockedConstrain));
        spiedJudge.setTimeoutFlag(true);
        assertEquals(false, spiedJudge.getTimeoutPass());

        spiedJudge.setTimeoutFlag(false);
        // No time file caused to timeout
        assertEquals(false, spiedJudge.getTimeoutPass());
    }
}