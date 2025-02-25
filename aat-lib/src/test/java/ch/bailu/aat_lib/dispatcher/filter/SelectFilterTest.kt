package ch.bailu.aat_lib.dispatcher.filter

import ch.bailu.aat_lib.dispatcher.usage.SelectableUsageTracker
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SelectFilterTest {

    @Test
    fun test() {
        val usageTracker = SelectableUsageTracker()
        var observedInfoID = 0

        val target = SelectFilter({ infoID, _ ->
            observedInfoID = infoID
        }, usageTracker)

        target.onContentUpdated(InfoID.OVERLAY, GpxInformation.NULL)
        target.onContentUpdated(InfoID.FILE_VIEW, GpxInformation.NULL)

        assertEquals(0, observedInfoID)

        usageTracker.select(InfoID.OVERLAY)
        target.onContentUpdated(InfoID.FILE_VIEW, GpxInformation.NULL)

        assertEquals(0, observedInfoID)
        target.onContentUpdated(InfoID.OVERLAY, GpxInformation.NULL)

        assertEquals(InfoID.OVERLAY, observedInfoID)
        target.onContentUpdated(InfoID.FILE_VIEW, GpxInformation.NULL)

        assertEquals(InfoID.OVERLAY, observedInfoID)

    }
}
