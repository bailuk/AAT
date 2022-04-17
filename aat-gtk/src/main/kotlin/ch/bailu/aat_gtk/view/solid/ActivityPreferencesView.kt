package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.VerticalScrollView
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause
import ch.bailu.aat_lib.preferences.location.SolidDistanceFilter
import ch.bailu.aat_lib.preferences.presets.SolidAccuracyFilter
import ch.bailu.aat_lib.preferences.presets.SolidMET
import ch.bailu.aat_lib.preferences.presets.SolidMissingTrigger
import ch.bailu.aat_lib.preferences.presets.SolidTrackerAutopause
import ch.bailu.aat_lib.resources.Res

class ActivityPreferencesView(storage: StorageInterface, index: Int) : VerticalScrollView() {
    val title = "${Res.str().p_preset()} ${index+1}"

    init {
        add(title)
        add(SolidEntryView(SolidMET(storage, index)).layout)
        add(SolidIndexComboView(SolidPostprocessedAutopause(storage, index)).layout)
        add(SolidIndexComboView(SolidTrackerAutopause(storage, index)).layout)
        add(SolidIndexComboView(SolidDistanceFilter(storage, index)).layout)
        add(SolidIndexComboView(SolidAccuracyFilter(storage, index)).layout)
        add(SolidIndexComboView(SolidMissingTrigger(storage, index)).layout)
    }
}
