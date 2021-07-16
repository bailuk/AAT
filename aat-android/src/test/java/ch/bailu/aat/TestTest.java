package ch.bailu.aat;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestTest {

    boolean initValue = false;

    @Before
    public void init() {
        initValue = true;
    }

    @Test
    public void testTest() {
        assertEquals("This test failed", true, initValue);
    }

    @After
    public void reset() {
        initValue = false;
    }
}
