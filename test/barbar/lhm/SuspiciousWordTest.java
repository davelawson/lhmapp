package barbar.lhm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SuspiciousWordTest  {
    @Test
    public void testConstructor() {
        SuspiciousWord sw = new SuspiciousWord(5, "word");
        assertEquals(5, sw.getIndex());
        assertEquals("word", sw.getWord());
    }

    @Test
    public void testCompareTo() {
        SuspiciousWord sw1 = new SuspiciousWord(5, "word1");
        SuspiciousWord sw2 = new SuspiciousWord(10, "word2");
        SuspiciousWord sw3 = new SuspiciousWord(2, "word3");
        SuspiciousWord sw4 = new SuspiciousWord(5, "word4");
        assertTrue(sw1.compareTo(sw2) < 0);
        assertTrue(sw1.compareTo(sw3) > 0);
        assertTrue(sw1.compareTo(sw4) == 0);
    }
}
