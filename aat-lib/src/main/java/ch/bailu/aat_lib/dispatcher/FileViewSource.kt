package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.foc.Foc

/**
 * Display a file. Usually selected from track/overlay list
 */
class FileViewSource : FileSource {
    constructor(appContext: AppContext) : super(appContext, InfoID.FILEVIEW)
    constructor(appContext: AppContext, file: Foc) : super(appContext, InfoID.FILEVIEW) {
        setFile(file)
    }

    override fun setFile(file: Foc) {
        super.setFile(file)
        setEnabled(true)
    }
}
