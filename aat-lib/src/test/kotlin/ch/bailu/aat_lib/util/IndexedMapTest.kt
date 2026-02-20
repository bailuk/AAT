package ch.bailu.aat_lib.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IndexedMapTest {

    @Test
    fun testIndexedMap() {
        val map = IndexedMap<String, String>()

        assertEquals(0, map.size())
        assertNull(map.getValueAt(0))
        assertNull(map.getKeyAt(0))
        assertNull(map.getValue("key"))

        map.put("key", "value")
        assertEquals(1, map.size())
        assertEquals("value", map.getValueAt(0))
        assertEquals("key", map.getKeyAt(0))
        assertEquals("value", map.getValue("key"))
        assertNull(map.getValue("blah"))

        assertNull(map.getValueAt(1))
        assertNull(map.getKeyAt(1))
        assertNull(map.getValueAt(-1))
        assertNull(map.getKeyAt(-1))

        assertEquals(0, map.indexOfKey("key"))
        assertEquals(-1, map.indexOfKey("value"))
    }
}
