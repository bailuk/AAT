package ch.bailu.aat

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TestTest {
    var initValue = false
    @Before
    fun init() {
        initValue = true
    }

    @Test
    fun testTest() {
        Assert.assertEquals("This test failed", true, initValue)
    }

    @After
    fun reset() {
        initValue = false
    }
}
