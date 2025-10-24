package ch.bailu.aat_lib.utli;

import ch.bailu.aat_lib.util.Limit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LimitTest {

    @Test
    public void testLimit() {
        assertEquals(5, Limit.biggest(4, 5));
        assertEquals(4, Limit.smallest(4, 5));
        assertEquals(5, Limit.clamp(5,4, 5));
        assertEquals(4, Limit.clamp(5,4, 4));
        assertTrue(Limit.isBetween(5,4, 6));
        assertFalse(Limit.isBetween(5,5, 6));
        assertFalse(Limit.isBetween(5,3, 4));

        assertTrue(Limit.isInRange(5,4, 6));
        assertTrue(Limit.isInRange(5,5, 6));
        assertTrue(Limit.isInRange(5,3, 5));
        assertFalse(Limit.isInRange(5,6, 6));
        assertFalse(Limit.isInRange(5,3, 4));

        assertEquals(10, Limit.biggest(4, 5, 10));
    }
}
