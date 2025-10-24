package ch.bailu.aat_lib.utli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.aat_lib.util.Limit;

public class LimitTest {

    @Test
    public void testLimit() {
        assertEquals(5, Limit.biggest(4, 5));
        assertEquals(4, Limit.smallest(4, 5));
        assertEquals(5, Limit.clamp(5,4, 5));
        assertEquals(4, Limit.clamp(5,4, 4));

        assertEquals(10, Limit.biggest(4, 5, 10));
    }
}
