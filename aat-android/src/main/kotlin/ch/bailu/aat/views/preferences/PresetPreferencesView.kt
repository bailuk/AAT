package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.presets.SolidBacklight
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause
import ch.bailu.aat_lib.preferences.location.SolidDistanceFilter
import ch.bailu.aat_lib.preferences.presets.SolidAccuracyFilter
import ch.bailu.aat_lib.preferences.presets.SolidMET
import ch.bailu.aat_lib.preferences.presets.SolidMissingTrigger
import ch.bailu.aat_lib.preferences.presets.SolidTrackerAutopause

class PresetPreferencesView(context: Context, index: Int, theme: UiTheme) :
    VerticalScrollView(context) {
    init {
        add(
            TitleView(context, context.getString(R.string.p_preset) + " " + (index + 1), theme)
        )
        val storage: StorageInterface = Storage(context)
        add(SolidStringView(context, SolidMET(storage, index), theme))
        add(SolidIndexListView(context, SolidPostprocessedAutopause(storage, index), theme))
        add(SolidIndexListView(context, SolidTrackerAutopause(storage, index), theme))
        add(SolidIndexListView(context, SolidDistanceFilter(storage, index), theme))
        add(SolidIndexListView(context, SolidAccuracyFilter(storage, index), theme))
        add(SolidIndexListView(context, SolidMissingTrigger(storage, index), theme))
        add(SolidIndexListView(context, SolidBacklight(context, index), theme))
    }
}
