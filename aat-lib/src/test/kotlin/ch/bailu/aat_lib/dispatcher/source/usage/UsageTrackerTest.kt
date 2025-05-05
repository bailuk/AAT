package ch.bailu.aat_lib.dispatcher.source.usage

import ch.bailu.aat_lib.dispatcher.usage.UsageTracker
import ch.bailu.aat_lib.gpx.information.InfoID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UsageTrackerTest {

    @Test
    fun test() {
       val usageTracker = UsageTracker()

        var countObserved = 0
        usageTracker.observe {
            countObserved++
        }

        assertEquals(0, countObserved)
        usageTracker.setEnabled(InfoID.FILE_VIEW, true)
        assertEquals(1, countObserved)

        assertTrue(usageTracker.isEnabled(InfoID.FILE_VIEW))
        assertFalse(usageTracker.isEnabled(InfoID.OVERLAY))

        usageTracker.setEnabled(InfoID.FILE_VIEW, false)
        assertEquals(2, countObserved)

        assertFalse(usageTracker.isEnabled(InfoID.FILE_VIEW))

        usageTracker.setEnabled(InfoID.FILE_VIEW, false)
        assertEquals(2, countObserved)

        usageTracker.setEnabled(InfoID.FILE_VIEW, true)
        assertEquals(3, countObserved)

        usageTracker.setEnabled(InfoID.FILE_VIEW, true)
        assertEquals(3, countObserved)

        usageTracker.disableAll()
        assertEquals(4, countObserved)
        assertFalse(usageTracker.isEnabled(InfoID.FILE_VIEW))
    }
}
