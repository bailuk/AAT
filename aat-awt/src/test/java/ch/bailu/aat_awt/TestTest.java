package ch.bailu.aat_awt;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTest {

    boolean initValue = false;

    @BeforeEach
    public void init() {
        initValue = true;
    }

    @Test
    public void testTest() {
        assertEquals(true, initValue);
    }

    @AfterEach
    public void reset() {
        initValue = false;
    }
}
