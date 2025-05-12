package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.lib.color.ARGB
import ch.bailu.aat_lib.service.elevation.tile.MultiCell
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HillShadeColorTableTest {

    @Test
    fun test() {
        val table = HillShadeColorTable()

        val color = ARGB(table.getColor(multiCell(0,0)))
        assertEquals(50, color.red())
        assertEquals(50, color.green())
        assertEquals(50, color.blue())
    }

    companion object {
        fun multiCell(dzx: Int, dzy: Int): MultiCell {
            return object : MultiCell() {
                override fun set(e: Int) {}
                override fun deltaZX(): Int {
                    return dzx
                }
                override fun deltaZY(): Int {
                    return dzy
                }
            }
        }
    }
}
