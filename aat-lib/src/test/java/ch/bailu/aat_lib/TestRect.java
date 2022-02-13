package ch.bailu.aat_lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.aat_lib.util.Rect;

public class TestRect {

    @Test
    public void testRect() {
        Rect rect = new Rect();

        assertEquals(1, rect.width());
        assertEquals(1, rect.height());
        assertEquals(0, rect.left);
        assertEquals(0, rect.right);
        assertEquals(0, rect.bottom);
        assertEquals(0, rect.top);

        rect.bottom = 1;
        rect.right = 2;

        assertEquals(3, rect.width());
        assertEquals(2, rect.height());

    }
}
