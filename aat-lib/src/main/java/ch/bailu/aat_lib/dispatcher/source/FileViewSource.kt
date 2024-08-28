package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.InfoID

/**
 * Display a file. Usually selected from track/overlay list
 */
class FileViewSource(appContext: AppContext, usageTracker: UsageTrackerInterface)
    : FileSource(appContext, InfoID.FILE_VIEW, usageTracker)
