package ch.bailu.aat_awt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bailu.aat_awt.preferences.AppPreferences;

public class TestPreferences {

    @Test
    public void testString() {
        AppPreferences p = new AppPreferences();
        p.writeString("test", "testContent");

        assertEquals("testContent", p.readString("test"));
    }
}
