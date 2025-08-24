package ch.bailu.aat_gtk.preferences

import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.system.SolidStartCount
import ch.bailu.aat_lib.util.fs.AppDirectory


class PreferenceLoadDefaults(context: AppContext) {
    init {
        val solidStartCount = SolidStartCount(context.storage)
        if (solidStartCount.isFirstRun) {
            setDefaults(context)
        }
        solidStartCount.increment()
    }

    private fun setDefaults(context: AppContext) {
        val solidRenderTheme = SolidRenderTheme(context.mapDirectory, context)
        SolidMapTileStack(solidRenderTheme).setDefaults()
        SolidWeight(context.storage).setDefaults()
        SolidPositionLock(context.storage, GtkCustomMapView.DEFAULT_KEY).setDefaults()
        SolidDirectoryQuery(context.storage, context).setValueFromString(AppDirectory.getTrackListDirectory(context.dataDirectory, 0).path)
    }
}
