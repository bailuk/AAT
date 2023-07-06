package ch.bailu.aat.menus

import android.view.Menu
import android.view.MenuItem
import ch.bailu.aat.R
import ch.bailu.aat.activities.PreferencesActivity
import ch.bailu.aat.app.ActivitySwitcher.Companion.start
import ch.bailu.aat.generated.Images
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.AndroidMapDirectories
import ch.bailu.aat.preferences.presets.SolidBacklight
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.views.preferences.dialog.SolidCheckListDialog
import ch.bailu.aat.views.preferences.dialog.SolidIndexListDialog
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.service.tracker.StateInterface
import ch.bailu.foc_android.FocAndroidFactory

class OptionsMenu(private val scontext: ServiceContext) : AbsMenu() {

    private var start: MenuItem? = null
    private var pause: MenuItem? = null
    override fun inflate(menu: Menu) {
        val c = scontext.getContext()

        start = add(menu,R.string.tracker_start) { scontext.insideContext { scontext.trackerService.onStartStop() }}.apply { setIcon(R.drawable.media_playback_start_inverse) }
        pause = add(menu, R.string.tracker_pause) {scontext.insideContext { scontext.trackerService.onPauseResume() }}.apply { setIcon(R.drawable.media_playback_pause_inverse) }

        add(menu, R.string.p_backlight_title) {
            SolidIndexListDialog(
                scontext.getContext(),
                SolidBacklight(c, SolidPreset(Storage(c)).index)
            )
        }
        add(menu, R.string.intro_settings) {start(c, PreferencesActivity::class.java)}
        add(menu, R.string.p_map) { val stheme = SolidRenderTheme(
            AndroidMapDirectories(c).createSolidDirectory(),
            FocAndroidFactory(c)
        )
            SolidCheckListDialog(
                c,
                SolidMapTileStack(stheme)
            )
        }
    }

    override val title: String
        get() = scontext.getContext().getString(R.string.app_sname)

    override fun prepare(menu: Menu) {
        scontext.insideContext { updateMenuText(scontext.trackerService) }
    }

    private fun updateMenuText(state: StateInterface) {
        start?.title = state.startStopText
        start?.setIcon(Images.get(state.startStopIcon))
        pause?.title = state.pauseResumeText
    }
}
