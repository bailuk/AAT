package ch.bailu.aat.preferences

import android.app.Activity
import android.content.Context
import ch.bailu.aat.activities.CockpitActivity
import ch.bailu.aat.activities.CockpitSplitActivity
import ch.bailu.aat.activities.MapActivity
import ch.bailu.aat.preferences.map.AndroidMapDirectories
import ch.bailu.aat.util.AppPermission
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidWeight
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.system.SolidStartCount
import ch.bailu.foc_android.FocAndroidFactory

class PreferenceLoadDefaults(context: Activity) {
    init {
        val storage = Storage(context)
        val startCount = SolidStartCount(storage)
        if (startCount.isFirstRun) {
            setDefaults(context, storage)
            AppPermission.requestFromUser(context)
        }
        startCount.increment()
    }

    private fun setDefaults(context: Context, storage: StorageInterface) {
        val stheme = SolidRenderTheme(
            AndroidMapDirectories(context).createSolidDirectory(),
            FocAndroidFactory(context)
        )
        SolidMapTileStack(stheme).setDefaults()
        SolidWeight(storage).setDefaults()
        SolidPositionLock(storage, MapActivity.SOLID_KEY).setDefaults()
        SolidPositionLock(storage, CockpitActivity.SOLID_KEY).setDefaults()
        SolidPositionLock(storage, CockpitSplitActivity.SOLID_MAP_KEY).setDefaults()
    }
}
