package ch.bailu.aat.activities

import android.content.Intent
import ch.bailu.aat.R
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.NameDescription
import ch.bailu.aat_lib.description.TrackSizeDescription
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

class OverlayListActivity : AbsGpxListActivity() {
    override val gpxListItemData: Array<ContentDescription>
        get() = arrayOf(
            DateDescription(),
            DistanceDescription(appContext.storage),
            NameDescription()
        )
    override val summaryData: Array<ContentDescription>
        get() = arrayOf(
            TrackSizeDescription()
        )

    override fun displayFile() {
        val intent = Intent(this, GpxEditorActivity::class.java)
        startActivity(intent)
    }

    override val directory: Foc
        get() = AppDirectory.getDataDirectory(
            SolidDataDirectory(AndroidSolidDataDirectoryDefault(this), FocAndroidFactory(this)),
            AppDirectory.DIR_OVERLAY
        )
    override val label: String
        get() = getString(R.string.intro_overlay_list)
}
