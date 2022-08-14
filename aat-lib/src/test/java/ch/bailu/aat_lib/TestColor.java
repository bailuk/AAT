package ch.bailu.aat_lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.aat_lib.lib.color.ARGB;
import ch.bailu.aat_lib.lib.color.ColorInterface;
import ch.bailu.aat_lib.lib.color.HSV;

public class TestColor {
    @Test
    public void testRgb() {
        ColorInterface color = new ARGB(6,7,8,9);
        assertEquals(6, color.alpha());
        assertEquals(7, color.red());
        assertEquals(8, color.green());
        assertEquals(9, color.blue());

        color = new ARGB(color.toInt());
        assertEquals(6, color.alpha());
        assertEquals(7, color.red());
        assertEquals(8, color.green());
        assertEquals(9, color.blue());

    }

    @Test
    public void testHsv() {
        ColorInterface color = new HSV(new ARGB(6,7,8,9));
        assertEquals(6, color.alpha());
        assertEquals(7, color.red());
        assertEquals(8, color.green());
        assertEquals(9, color.blue());

        color = new ARGB(color.toInt());
        assertEquals(6, color.alpha());
        assertEquals(7, color.red());
        assertEquals(8, color.green());
        assertEquals(9, color.blue());

    }

}
