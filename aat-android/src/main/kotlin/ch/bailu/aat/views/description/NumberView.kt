package ch.bailu.aat.views.description

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ch.bailu.aat.R
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog

import kotlin.math.roundToInt

open class NumberView(context: Context, data: ContentDescription, private val theme: UiTheme) :
    ViewGroup(context), TargetInterface {
    private val label: TextView
    private val number: TextView
    private val unit: TextView
    val description: ContentDescription
    private val defaultTextSize: Float

    init {
        number = createLabel()
        theme.header(number)
        number.typeface = Typeface.create(null as String?, Typeface.BOLD)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            number.hyphenationFrequency = Layout.HYPHENATION_FREQUENCY_NONE
        }
        label = createLabel()
        theme.content(label)
        defaultTextSize = label.textSize
        unit = createLabel()
        setDefaultUnitLabelColor()
        description = data
        updateAllText()
    }

    fun setDefaultUnitLabelColor() {
        theme.contentAlt(unit)
        theme.backgroundAlt(unit)
    }

    fun setHighlightUnitLabelColor() {
        setHighlightUnitLabelColor(theme.getHighlightColor())
    }

    private fun setHighlightUnitLabelColor(color: Int) {
        unit.setBackgroundColor(color)
        unit.setTextColor(Color.BLACK)
    }

    private fun createLabel(): TextView {
        val view = TextView(context)
        view.setPadding(0, 0, 0, 0)
        view.includeFontPadding = false
        addView(view)
        return view
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            Layouter(l, t, r, b).layout()
        }
    }

    private inner class Layouter(val width: Int, val height: Int) {
        val labelHeight: Int
        val margin: Int
        val X = 5f


        constructor(l: Int, t: Int, r: Int, b: Int) : this(r - l, b - t)

        init {
            var margin = height / 10
            var labelHeight = toHeight(defaultTextSize)
            var numberHeight = height - labelHeight - labelHeight - margin * 2
            if (numberHeight < labelHeight) {
                numberHeight = height - labelHeight - labelHeight
                margin = 0
            }
            if (numberHeight < labelHeight) {
                numberHeight = toHeight(defaultTextSize)
                labelHeight = (height - numberHeight) / 2
            }
            this.labelHeight = labelHeight
            this.margin = margin
            setTextSize(label, toTextSize(labelHeight))
            setTextSize(unit, toTextSize(labelHeight))
            setTextSize(number, toTextSize(numberHeight))
        }

        private fun toTextSize(height: Int): Float {
            return X * height.toFloat() / (X + 1)
        }

        private fun toHeight(textSize: Float): Int {
            return (textSize + textSize / X).roundToInt()
        }

        private fun setTextSize(v: TextView, s: Float) {
            if (s > 0) {
                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, s)
            }
        }

        fun layout() {
            val l = 0
            val t = 0
            val b = height
            val r = width
            var height = height
            label.measure(width, height)
            label.layout(l, t + margin, r, label.measuredHeight + margin)
            height -= label.measuredHeight
            unit.measure(width, height)
            unit.layout(l, b - unit.measuredHeight - margin, r, b - margin)
            height -= unit.measuredHeight
            height -= margin * 2
            number.measure(width, height)
            number.layout(l, label.measuredHeight + margin, r, b - unit.measuredHeight - margin)
        }

    }

    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        description.onContentUpdated(iid, info)
        updateAllText()
    }

    fun updateAllText() {
        number.text = description.getValue()
        label.text = description.getLabelShort()
        unit.text = description.getUnit()
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        super.setOnClickListener(listener)
        theme.button(this)
        setPadding(0, 0, 0, 0)
    }

    fun requestOnClickSensorReconnect(): View {
        ToolTip.set(this, R.string.sensor_connect)
        setOnClickListener { v: View ->
            AppLog.i(v.context, v.context.getString(R.string.sensor_connect))
            AndroidBroadcaster.broadcast(
                v.context,
                AppBroadcaster.SENSOR_RECONNECT + InfoID.SENSORS
            )
        }
        return this
    }
}
