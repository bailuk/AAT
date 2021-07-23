package ch.bailu.aat_lib;

import org.junit.jupiter.api.Test;

import ch.bailu.aat_lib.util.Limit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLimit {
    @Test
    public void testLimit() {
        assertEquals(20, Limit.clamp(22, 1, 20));
        assertEquals(1, Limit.clamp(-200, 1, 20));
        assertEquals(-4, Limit.smallest(5,3,6,234,7,-4, 45345));
        assertEquals(-5.3, Limit.smallest(-5.3,3d,6d,234d,7d,-4.3433, 45345.34));

        assertEquals(45345, Limit.biggest(5,3,6,234,7,-4, 45345));
        assertEquals(45345.34, Limit.biggest(-5.3,3d,6d,234d,7d,-4.3433, 45345.34));

    }
}
