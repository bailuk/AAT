package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.InfoID

/**
 * Display a file. Usually selected from track/overlay list
 */
class FileViewSource : FileSource {
    constructor(appContext: AppContext, usageTracker: UsageTrackerInterface)
            : super(appContext, InfoID.FILE_VIEW, usageTracker)

    /*
    constructor(appContext: AppContext, file: Foc) : super(appContext, InfoID.FILE_VIEW) {
        setFile(file)
    }

     */
}
