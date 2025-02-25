package ch.bailu.aat_lib.lib.filter_list

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KeyListTest {

    @Test
    fun test() {
        val keyList1 = KeyList()
        assertTrue(keyList1.isEmpty)
        assertFalse(keyList1.hasKey("amenity"))

        val keyList2 = KeyList("amenity emergency leisure shop sport tourism name")
        assertFalse(keyList2.isEmpty)

        assertTrue(keyList2.hasKey("Amenity"))
        assertFalse(keyList2.hasKey("amenity2"))

        assertTrue(keyList2.fits(keyList1))
    }
}
