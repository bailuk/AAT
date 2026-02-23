package ch.bailu.aat_lib.service.icons

import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IconMapParserTest {

    @Test
    fun testMapParser() {
        val map = IconMap()
        IconMapParser(FocResource("icons/iconmap.txt"), map)

        val icon1 = map.get(Keys.toIndex("tourism"), "camp_site")
        val icon2 = map.get(Keys.toIndex("tourism"), "caravan_site")
        val icon3 = map.get(Keys.toIndex("tourism"), "alpine_hut")
        val icon4 = map.get(Keys.toIndex("tourism"), "camp_side")
        val icon5 = map.get(Keys.toIndex("guest_house"), "bed_and_breakfast")

        assertEquals("icons/accommodation/camping.svg", icon1?.svg)
        assertEquals("icons/accommodation/caravan_park.svg", icon2?.svg)
        assertEquals("icons/accommodation/alpinehut.svg", icon3?.svg)
        assertNull(icon4?.svg)
        assertEquals("icons/accommodation/bed_and_breakfast.svg", icon5?.svg)

        val builder = StringBuilder()
        map.appendStatusText(builder)
        assertEquals("IconMap (key_list) size: 2<br>", builder.toString())
    }
}
