package ch.bailu.aat_lib.util

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestTest {
    var initValue: Boolean = false

    @BeforeEach
    fun init() {
        initValue = true
    }

    @Test
    fun testTest() {
        Assertions.assertEquals(true, initValue)
    }

    @AfterEach
    fun reset() {
        initValue = false
    }
}
