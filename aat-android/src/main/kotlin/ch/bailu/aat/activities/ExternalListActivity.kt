package ch.bailu.aat.activities

import android.content.Intent
import ch.bailu.aat.R
import ch.bailu.aat.preferences.system.SolidExternalDirectory
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.NameDescription
import ch.bailu.aat_lib.description.TrackSizeDescription
import ch.bailu.foc.Foc

class ExternalListActivity : AbsGpxListActivity() {
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
        val intent = Intent(this, FileContentActivity::class.java)
        startActivity(intent)
    }

    override val directory: Foc
        get() = SolidExternalDirectory(this).getValueAsFile()
    override val label: String
        get() = getString(R.string.intro_external_list)
}
