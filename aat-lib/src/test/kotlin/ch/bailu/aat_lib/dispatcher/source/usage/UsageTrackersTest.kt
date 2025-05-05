package ch.bailu.aat_lib.dispatcher.source.usage

import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.mock.MockStorage
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileEnabled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UsageTrackersTest {

    @Test
    fun test() {
        val usageTrackers = UsageTrackers()
        var observed = 0

        usageTrackers.observe { observed++ }

        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)

        val tracker1 = usageTrackers.createTracker()
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)

        tracker1.setEnabled(InfoID.FILE_VIEW, false)
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)


        tracker1.setEnabled(InfoID.FILE_VIEW, true)
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(1, observed)


        val tracker2 = usageTrackers.createTracker()
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(1, observed)


        tracker1.setEnabled(InfoID.FILE_VIEW, false)
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(2, observed)


        tracker2.setEnabled(InfoID.FILE_VIEW, true)
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(3, observed)
    }

    @Test
    fun testSelectable() {
        val usageTrackers = UsageTrackers()
        var observed = 0

        usageTrackers.observe { observed++ }

        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)

        val tracker1 = usageTrackers.createTracker()
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)

        tracker1.setEnabled(InfoID.FILE_VIEW, false)
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)


        tracker1.setEnabled(InfoID.FILE_VIEW, true)
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(1, observed)


        val tracker2 = usageTrackers.createSelectableUsageTracker()
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(1, observed)


        tracker1.setEnabled(InfoID.FILE_VIEW, false)
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(2, observed)


        tracker2.select(InfoID.FILE_VIEW)
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(3, observed)

        tracker2.select(InfoID.OVERLAY)
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertTrue(usageTrackers.isEnabled(InfoID.OVERLAY))
        assertEquals(5, observed)
    }


    @Test
    fun testOverlay() {
        val usageTrackers = UsageTrackers()
        var observed = 0

        usageTrackers.observe { observed++ }

        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)

        val tracker1 = usageTrackers.createTracker()
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)

        tracker1.setEnabled(InfoID.FILE_VIEW, false)
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)


        tracker1.setEnabled(InfoID.FILE_VIEW, true)
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(1, observed)


        val storage = MockStorage()
        val tracker2 = usageTrackers.createOverlayUsageTracker(storage,
            InfoID.FILE_VIEW,
            InfoID.OVERLAY
        )
        val solidOverlayFileView = SolidOverlayFileEnabled(storage, InfoID.FILE_VIEW)
        val solidOverlayOverlay = SolidOverlayFileEnabled(storage, InfoID.OVERLAY)

        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(1, observed)


        tracker1.setEnabled(InfoID.FILE_VIEW, false)
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(2, observed)

        assertEquals(0, storage.mockIntValue)
        assertFalse(tracker2.isEnabled(InfoID.FILE_VIEW))

        solidOverlayFileView.value = true
        assertEquals(1, storage.mockIntValue)
        assertTrue(tracker2.isEnabled(InfoID.FILE_VIEW))
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(3, observed)

        solidOverlayFileView.value = false
        solidOverlayOverlay.value = true
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertTrue(usageTrackers.isEnabled(InfoID.OVERLAY))
        assertEquals(5, observed)
    }

    @Test
    fun testOverlaySimple() {
        var observed = 0

        val storage = MockStorage()
        val usageTrackers = UsageTrackers()

        val solidOverlayFileView = SolidOverlayFileEnabled(storage, InfoID.FILE_VIEW)
        val solidOverlayOverlay = SolidOverlayFileEnabled(storage, InfoID.OVERLAY)
        solidOverlayOverlay.value = true

        val overlayUsageTracker = usageTrackers.createOverlayUsageTracker(storage,
            InfoID.FILE_VIEW,
            InfoID.OVERLAY
        )
        usageTrackers.observe { observed++ }

        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertTrue(overlayUsageTracker.isEnabled(InfoID.FILE_VIEW))
        assertEquals(0, observed)

        solidOverlayFileView.value = false
        assertFalse(overlayUsageTracker.isEnabled(InfoID.FILE_VIEW))
        assertFalse(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertEquals(1, observed)

        solidOverlayFileView.value = true
        solidOverlayOverlay.value = false
        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
        assertFalse(usageTrackers.isEnabled(InfoID.OVERLAY))
        assertEquals(3, observed)


        solidOverlayFileView.value = false
        val selectableUsageTracker = usageTrackers.createSelectableUsageTracker()
        selectableUsageTracker.select(InfoID.FILE_VIEW)

        assertTrue(usageTrackers.isEnabled(InfoID.FILE_VIEW))
    }
}
