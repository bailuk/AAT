package ch.bailu.aat.views.busy

import android.R
import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat_lib.map.AppDensity

class BusyView(context: Context) : LinearLayout(context) {
    private val label: TextView

    init {
        orientation = HORIZONTAL
        val density: AppDensity = AndroidAppDensity(context)
        val m = density.toPixelInt(2f)
        val lp =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.setMargins(m, m, m, m)
        val progress = ProgressBar(context, null, R.attr.progressBarStyleSmall)
        addView(progress, lp)
        label = TextView(context)
        addView(label, lp)
    }

    fun setText(text: CharSequence) {
        label.text = text
    }
}
