package ch.bailu.aat.activities

import android.os.Bundle
import ch.bailu.aat.util.ui.Backlight
import ch.bailu.aat_lib.gpx.information.InfoID

abstract class AbsKeepScreenOnActivity : ActivityContext() {
    private var backlight: Backlight? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backlight = Backlight(this, serviceContext).apply {
            dispatcher.addTarget(this, InfoID.TRACKER)
        }
    }

    override fun onResumeWithService() {
        super.onResumeWithService()
        backlight?.setBacklightAndPreset()
    }

    override fun onDestroy() {
        backlight?.close()
        backlight = null
        super.onDestroy()
    }
}
