package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.util.extensions.setTooltipText
import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.EntryRow

class SolidEntryView(private val solid: AbsSolidType) : OnPreferencesChanged {
    val layout = EntryRow()
    private var blockUpdate = false

    init {
        layout.setTitle(solid.getLabel())
        layout.asEditable().setText(solid.getValueAsString())

        layout.asEditable().onChanged {
            val value = layout.asEditable().text

            if (value.isNotNull) {
                blockUpdate = true
                try {
                    solid.setValueFromString(value.toString())
                    AppLog.d(this, value.toString())
                    layout.removeCssClass("error")
                } catch (e: ValidationException) {
                    layout.addCssClass("error")
                    AppLog.e(this, "${e.message}: $value")
                }
                blockUpdate = false
            }
        }

        solid.register(this)

        layout.onDestroy {
            solid.unregister(this)
            layout.disconnectSignals()
        }
        layout.setTooltipText(solid)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (!blockUpdate && solid.hasKey(key)) {
            layout.asEditable().setText((solid.getValueAsString()))
        }
    }
}
