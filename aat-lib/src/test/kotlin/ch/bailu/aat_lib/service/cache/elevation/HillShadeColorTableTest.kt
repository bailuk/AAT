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

        val col = preMultipliedAlpha(color.alpha(),50)
        assertEquals(col, color.red())
        assertEquals(col, color.green())
        assertEquals(col, color.blue())
    }

    private fun preMultipliedAlpha(alpha: Int, color: Int): Int {
        return ((color * alpha).toDouble() / 255.0).toInt()
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
