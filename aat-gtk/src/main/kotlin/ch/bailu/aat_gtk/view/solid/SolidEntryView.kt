package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.util.extensions.setTooltipText
import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.EntryRow

class SolidEntryView(private val solid: AbsSolidType) : OnPreferencesChanged {
    val layout = EntryRow()

    private var blockUpdate = false
    private var errorMessage = ""

    init {
        layout.setTitle(solid.getLabel())
        layout.asEditable().setText(solid.getValueAsString())
        layout.setTooltipText(solid)
        layout.asEditable().onChanged {
            val value = layout.asEditable().text

            if (value.isNotNull) {
                blockUpdate = true
                try {
                    solid.setValueFromString(value.toString())
                    clearError()
                } catch (e: ValidationException) {
                    setError(e)
                }
                blockUpdate = false
            }
        }
        layout.onDestroy {
            solid.unregister(this)
            layout.disconnectSignals()
        }
        solid.register(this)
    }

    private fun clearError() {
        if (errorMessage.isNotEmpty()) {
            errorMessage = ""
            layout.removeCssClass(Strings.CSS_ERROR)
            layout.setTitle(solid.getLabel())
        }
    }

    private fun setError(e: ValidationException) {
        val message = e.message
        if (message is String && message != errorMessage) {
            errorMessage = message
            layout.addCssClass(Strings.CSS_ERROR)
            layout.setTitle("${solid.getLabel()} - $errorMessage")
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (!blockUpdate && solid.hasKey(key)) {
            layout.asEditable().setText((solid.getValueAsString()))
        }
    }
}
