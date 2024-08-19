package ch.bailu.aat.views.tileremover

import android.graphics.Typeface
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.preferences.map.SolidTrimIndex
import ch.bailu.aat_lib.service.tileremover.SourceSummaryInterface
import ch.bailu.aat.util.ui.theme.UiTheme

class TileSummaryView(private val parent: RadioGroup, id: Int, theme: UiTheme) :
    View.OnClickListener {
    private val radioButton: RadioButton = RadioButton(parent.context)
    private val textView: TextView

    init {
        radioButton.id = id
        radioButton.setOnClickListener(this)
        textView = TextView(parent.context)
        parent.addView(radioButton)
        parent.addView(textView)
        theme.content(textView)
        theme.content(radioButton)
        radioButton.setTypeface(null, Typeface.BOLD)
    }

    fun destroy() {
        parent.removeView(radioButton)
        parent.removeView(textView)
    }

    fun setTitle(title: String) {
        radioButton.text = title
    }

    fun displaySummaryReport(builder: StringBuilder, summary: SourceSummaryInterface) {
        builder.setLength(0)
        textView.text = summary.buildReport(builder).toString()
    }

    fun select() {
        radioButton.toggle()
    }

    fun select(selected: Int) {
        if (radioButton.id == selected) {
            select()
        }
    }

    override fun onClick(v: View) {
        SolidTrimIndex(Storage(parent.context)).setValue(v.id)
    }
}
