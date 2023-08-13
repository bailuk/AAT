package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface

abstract class AbsSolidView(context: Context, private val solid: AbsSolidType, theme: UiTheme) :
    LabelTextView(context, solid.getLabel(), theme), OnPreferencesChanged {
    init {
        theme.button(this)
        setOnClickListener { onRequestNewValue() }
    }

    abstract fun onRequestNewValue()
    fun setText() {
        setText("[$solid]")
        setToolTip(solid)
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        solid.register(this)
        setText()
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            setText()
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        solid.unregister(this)
    }
}
