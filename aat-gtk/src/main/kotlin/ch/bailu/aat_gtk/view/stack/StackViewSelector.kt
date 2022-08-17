package ch.bailu.aat_gtk.view.stack

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin

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

        combo.margin(Layout.margin)
        selectFromPreferences()
    }

    private fun selectFromPreferences() {
        val selected = stack.storage.readInteger(stack.key)

        if (combo.active != selected) {
            combo.active = selected
        }
    }
}
