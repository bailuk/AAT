package ch.bailu.aat

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestTest {
    var initValue = false
    @BeforeEach
    fun init() {
        initValue = true
    }

    @Test
    fun testTest() {
        Assertions.assertEquals(true, initValue, "This test failed")
    }

    @AfterEach
    fun reset() {
        initValue = false
    }
}
