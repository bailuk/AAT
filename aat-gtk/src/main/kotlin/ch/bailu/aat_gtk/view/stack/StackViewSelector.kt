package ch.bailu.aat_gtk.view.stack

import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.solid.PreferencesStackView

class StackViewSelector(private val stack: LazyStackView) {
    val combo = stack.createCombo()

    init {
        stack.storage.register { _, key ->
            if (key == stack.key) {
                selectFromPreferences()
            }
        }

        combo.onChanged {
            stack.show(combo.active)
        }

        combo.margin(PreferencesStackView.MARGIN)
        selectFromPreferences()
    }

    private fun selectFromPreferences() {
        val selected = stack.storage.readInteger(stack.key)

        if (combo.active != selected) {
            combo.active = selected
        }
    }
}
