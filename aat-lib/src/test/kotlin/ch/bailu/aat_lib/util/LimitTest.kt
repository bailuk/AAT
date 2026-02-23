package ch.bailu.aat_lib.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LimitTest {
    @Test
    fun testLimit() {
        Assertions.assertEquals(5, Limit.biggest(4, 5))
        Assertions.assertEquals(4, Limit.smallest(4, 5))
        Assertions.assertEquals(5, Limit.clamp(5, 4, 5))
        Assertions.assertEquals(4, Limit.clamp(5, 4, 4))

        Assertions.assertEquals(10, Limit.biggest(4, 5, 10))
    }


    @Test
    fun testLimit2() {
        Assertions.assertEquals(20, Limit.clamp(22, 1, 20))
        Assertions.assertEquals(1, Limit.clamp(-200, 1, 20))
        Assertions.assertEquals(-4, Limit.smallest(5, 3, 6, 234, 7, -4, 45345))
        Assertions.assertEquals(-5.3, Limit.smallest(-5.3, 3.0, 6.0, 234.0, 7.0, -4.3433, 45345.34))

        Assertions.assertEquals(45345, Limit.biggest(5, 3, 6, 234, 7, -4, 45345))
        Assertions.assertEquals(45345.34, Limit.biggest(-5.3, 3.0, 6.0, 234.0, 7.0, -4.3433, 45345.34))
    }
}
