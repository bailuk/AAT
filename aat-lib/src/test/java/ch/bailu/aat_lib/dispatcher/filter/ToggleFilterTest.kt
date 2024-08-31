package ch.bailu.aat_lib.dispatcher.filter

import ch.bailu.aat_lib.dispatcher.usage.SelectableUsageTracker
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ToggleFilterTest {

    @Test
    fun test() {
        val usageTracker = SelectableUsageTracker()
        var observedInfoID = 0
        var observedGpxInformation = GpxInformation.NULL

        val target = ToggleFilter({ infoID, information ->
            observedGpxInformation = information
            observedInfoID = infoID

        }, InfoID.FILE_VIEW, usageTracker)

        target.onContentUpdated(InfoID.OVERLAY, GpxInformation.NULL)
        target.onContentUpdated(InfoID.FILE_VIEW, GpxInformation.NULL)

        assertEquals(InfoID.FILE_VIEW, observedInfoID)

        usageTracker.select(InfoID.OVERLAY)
        target.onContentUpdated(InfoID.FILE_VIEW, GpxInformation.NULL)

        assertEquals(InfoID.FILE_VIEW, observedInfoID)
        target.onContentUpdated(InfoID.OVERLAY, GpxInformation.NULL)

        assertEquals(InfoID.FILE_VIEW, observedInfoID)
        target.onContentUpdated(InfoID.FILE_VIEW, GpxInformation.NULL)

        assertEquals(InfoID.FILE_VIEW, observedInfoID)
    }
}
