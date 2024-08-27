package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.mock.MockAppContext
import ch.bailu.foc.FocName
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FileSourceTest {

    @Test
    fun test() {
        val appContext = MockAppContext()
        val usageTrackers = UsageTrackers()

        val fileSource = FileSource(appContext, InfoID.FILE_VIEW, usageTrackers)
        assertEquals(InfoID.FILE_VIEW, fileSource.getIID())

        fileSource.onResumeWithService()
        fileSource.setFile(FocName("test.gpx"))

        val selectableUsageTracker = usageTrackers.createSelectableUsageTracker()
        selectableUsageTracker.select(InfoID.FILE_VIEW)


    }
}
