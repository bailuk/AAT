package ch.bailu.aat_gtk.view.preferences

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.location.SolidDistanceFilter
import ch.bailu.aat_lib.preferences.presets.SolidAccuracyFilter
import ch.bailu.aat_lib.preferences.presets.SolidMET
import ch.bailu.aat_lib.preferences.presets.SolidMissingTrigger
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.ExpanderRow
import ch.bailu.gtk.adw.PreferencesGroup

class ActivityPreferencesPage(storage: StorageInterface): PreferencesPageParent(Res.str().p_preset(), "presets") {
    init {
        page.add(PreferencesGroup().apply {
            setTitle(Res.str().p_preset())

            val presetCount = SolidPresetCount(storage)

            for(index in 0 until  presetCount.value) {
                add(ExpanderRow().apply {
                    setTitle("${Res.str().p_preset()} ${index+1}")
                    setSubtitle(SolidMET(storage, index).getValueAsString())
                    addRow(SolidEntryView(SolidMET(storage, index)).layout)
                    addRow(SolidIndexComboRowView(SolidPostprocessedAutopause(storage, index)).layout)
                    addRow(SolidIndexComboRowView(SolidDistanceFilter(storage, index)).layout)
                    addRow(SolidIndexComboRowView(SolidAccuracyFilter(storage, index)).layout)
                    addRow(SolidIndexComboRowView(SolidMissingTrigger(storage, index)).layout)
                })
            }
        })
    }
}
