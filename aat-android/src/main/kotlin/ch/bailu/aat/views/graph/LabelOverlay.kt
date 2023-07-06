package ch.bailu.aat.views.graph

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat_lib.map.MapColor
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.aat_lib.view.graph.LabelInterface

class LabelOverlay(context: Context, gravity: Int) : LinearLayout(context), LabelInterface {
    private val labels = IndexedMap<Int, TextView>()
    private val defaultTextSize: Float

    init {
        orientation = VERTICAL
        setGravity(gravity)
        defaultTextSize = TextView(context).textSize
    }

    fun setTextSizeFromHeight(height: Int) {
        if (labels.size() > 0) {
            var size = (height / labels.size()).toFloat()
            size -= size / 3f
            size = Math.min(defaultTextSize, size)
            for (i in 0 until labels.size()) {
                labels.getValueAt(i)?.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
            }
        }
    }

    override fun setText(color: Int, label: String, unit: String) {
        setText(color, "$label [$unit]")
    }

    fun setText(color: Int, label: Int, unit: String) {
        setText(color, context.getString(label), unit)
    }

    override fun setText(color: Int, text: String) {
        val view = labels.getValue(color)
        if (view == null) {
            labels.put(color, addLabel(color, text))
        } else {
            view.text = text
        }
    }

    private fun addLabel(color: Int, text: String): TextView {
        val v = TextView(context)
        v.text = text
        v.setTextColor(color)
        v.setBackgroundColor(BACKGROUND_COLOR)
        addView(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        return v
    }

    companion object {
        private val BACKGROUND_COLOR = MapColor.setAlpha(Color.BLACK, 100)
    }
}
