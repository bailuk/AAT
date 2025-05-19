package ch.bailu.aat.views.osm

import android.text.Selection
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.bar.ControlBar

class EditTextTool(val edit: EditText, orientation: Int, theme: UiTheme) : LinearLayout(
    edit.context
), View.OnClickListener {
    val bar: ControlBar = ControlBar(context, orientation, theme, 6)
    private val clearText: ImageButtonViewGroup = bar.addImageButton(R.drawable.edit_clear_all_inverse)

    init {
        clearText.setOnClickListener(this)
        ToolTip.set(clearText, R.string.tt_nominatim_clear)
        setOrientation(HORIZONTAL)
        addView(
            edit, LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT, 100f
            )
        )
        add(bar)
    }

    override fun onClick(view: View) {
        if (view === clearText) {
            CurrentLine().delete()
        }
    }

    fun add(view: View) {
        addView(
            view,
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, 1f
            )
        )
    }

    fun insertLine(s: String) {
        CurrentLine().insert(s)
    }

    inner class CurrentLine {
        var start = 0
        var end = 0

        init {
            val selectionStart = Selection.getSelectionStart(edit.text)
            if (selectionStart != -1) {
                val layout = edit.layout
                val line = layout.getLineForOffset(selectionStart)
                start = layout.getLineStart(line)
                end = layout.getLineEnd(line)
            } else {
                end = -1
                start = end
            }
        }

        fun delete() {
            if (start > -1) edit.text.delete(start, end)
        }

        fun insert(l: String) {
            if (start > -1) {
                edit.text.insert(
                    end, """
     $l
     
     """.trimIndent()
                )
            }
        }
    }
}
