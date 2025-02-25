package ch.bailu.aat_lib.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestRect {
    @Test
    fun testRect() {
        val rect = Rect()

        Assertions.assertEquals(1, rect.width)
        Assertions.assertEquals(1, rect.height)
        Assertions.assertEquals(0, rect.left)
        Assertions.assertEquals(0, rect.right)
        Assertions.assertEquals(0, rect.bottom)
        Assertions.assertEquals(0, rect.top)

        rect.bottom = 1
        rect.right = 2

        Assertions.assertEquals(3, rect.width)
        Assertions.assertEquals(2, rect.height)
    }
}
