package ch.bailu.aat.activities

import android.content.Intent
import ch.bailu.aat.R
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.NameDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.description.TrackSizeDescription
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.foc.Foc

class TrackListActivity : AbsGpxListActivity() {
    override val gpxListItemData: Array<ContentDescription>
        get() = arrayOf(
            DateDescription(),
            DistanceDescription(appContext.storage),
            AverageSpeedDescription(appContext.storage),
            TimeDescription(),
            NameDescription()
        )
    override val summaryData: Array<ContentDescription>
        get() = arrayOf(
            TrackSizeDescription(),
            MaximumSpeedDescription(appContext.storage),
            DistanceDescription(appContext.storage),
            AverageSpeedDescription(appContext.storage),
            TimeDescription()
        )

    override fun displayFile() {
        val intent = Intent(this, FileContentActivity::class.java)
        startActivity(intent)
    }

    override val directory: Foc
        get() = SolidPreset(appContext.storage).getDirectory(AndroidSolidDataDirectory(this))
    override val label: String
        get() = getString(R.string.intro_list)
}
