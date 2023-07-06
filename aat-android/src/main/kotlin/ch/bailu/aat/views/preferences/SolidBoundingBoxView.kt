package ch.bailu.aat.views.preferences

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidBoundingBox
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidBoundingBoxView(
    context: Context,
    private val sbounding: SolidBoundingBox,
    mc: MapContext,
    theme: UiTheme
) : LabelTextView(context, sbounding.label, theme), OnPreferencesChanged {
    init {
        setText(sbounding.valueAsString)
        theme.button(this)
        setOnClickListener { sbounding.value = BoundingBoxE6(mc.metrics.boundingBox) }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sbounding.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sbounding.hasKey(key)) {
            setText(sbounding.valueAsString)
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        sbounding.unregister(this)
    }
}
