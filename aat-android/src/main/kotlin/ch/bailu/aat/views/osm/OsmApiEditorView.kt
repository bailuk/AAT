package ch.bailu.aat.views.osm

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.preferences.TitleView
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration

class OsmApiEditorView(context: Context, osmApi: OsmApiConfiguration, theme: UiTheme) :
    LinearLayout(context) {
    private val editor: EditTextTool
    private val preview: TextView
    private val inputMultiView: MultiView

    init {
        orientation = VERTICAL
        preview = TextView(getContext())
        val scroller = VerticalScrollView(getContext())
        scroller.add(preview)
        editor = EditTextTool(
            TagEditor(
                getContext(),
                osmApi.baseDirectory
            ), VERTICAL, theme
        )
        inputMultiView = MultiView(getContext(), osmApi.apiName)
        inputMultiView.add(editor)
        inputMultiView.add(scroller)
        preview.setOnClickListener { inputMultiView.setNext() }
        preview.text = osmApi.getUrlPreview(editor.edit.text.toString())
        theme.content(preview)
        addView(createTitle(osmApi, theme))
        addView(inputMultiView)
    }

    private fun createTitle(osmApi: OsmApiConfiguration, theme: UiTheme): View {
        val layout = LinearLayout(context)
        layout.orientation = HORIZONTAL
        val strings = osmApi.urlStart.split(osmApi.apiName.lowercase().toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val b: TextView = TitleView(context, osmApi.apiName, AppTheme.search)
        b.setSingleLine()
        if (strings.size > 1) {
            val a = TextView(context)
            val c = TextView(context)
            a.text = strings[0]
            a.setSingleLine()
            c.text = strings[1]
            c.setSingleLine()
            c.ellipsize = TextUtils.TruncateAt.END
            theme.content(a)
            theme.content(c)
            b.setTextColor(theme.getHighlightColor())
            layout.addView(a)
            layout.addView(b)
            layout.addView(c)
        } else {
            layout.addView(b)
        }
        layout.setOnClickListener {
            preview.text = osmApi.getUrlPreview(editor.edit.text.toString())
            inputMultiView.setNext()
        }
        return layout
    }

    override fun toString(): String {
        return editor.edit.text.toString()
    }

    fun insertLine(text: String) {
        editor.insertLine(text)
        inputMultiView.setActive(0)
    }

    fun setText(query: String) {
        editor.edit.setText(query)
        inputMultiView.setActive(0)
    }
}
