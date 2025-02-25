package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.mock.MockStorage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestSolidCacheSize {

    @Test
    fun testValues() {
        val max = Math.min(Runtime.getRuntime().maxMemory(), SolidCacheSize.MAX_CACHE_SIZE)
        val storage = MockStorage()
        val solid = SolidCacheSize(storage)

        assertEquals(11, solid.length())
        solid.index = 10
        assertEquals(10, solid.index)
        assertEquals(max, solid.valueAsLong)


        val strings = solid.getStringArray()
        assertEquals(11, strings.size)
    }
}
