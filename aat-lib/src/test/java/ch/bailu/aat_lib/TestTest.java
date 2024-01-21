package ch.bailu.aat_lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
