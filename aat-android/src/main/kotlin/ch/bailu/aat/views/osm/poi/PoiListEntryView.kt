package ch.bailu.aat.views.osm.poi

import android.content.Context
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.osm.features.OnSelected
import ch.bailu.aat_lib.search.poi.PoiListItem

class PoiListEntryView(context: Context, onSelected: OnSelected, theme: UiTheme) :
    LinearLayout(context) {
    private var listItem: PoiListItem? = null
    private val checkBox: CheckBox
    private val textView: TextView

    init {
        setOnClickListener {
            val item = listItem
            if (item is PoiListItem) {
                onSelected.onSelected(item, OnSelected.Action.Edit, "")
            }
        }
        checkBox = CheckBox(getContext())
        checkBox.setOnCheckedChangeListener { _: CompoundButton?, _: Boolean -> }
        checkBox.isClickable = false
        textView = TextView(getContext())
        AppTheme.padding(textView, 10)
        theme.content(checkBox)
        theme.header(textView)
        theme.button(textView)
        addView(checkBox)
        addView(textView)
    }

    fun set(item: PoiListItem) {
        listItem = item
        if (item.isSummary) {
            checkBox.visibility = GONE
            checkBox.text = ""
            textView.text = item.title
            textView.visibility = VISIBLE
        } else {
            textView.visibility = GONE
            textView.text = ""
            checkBox.isChecked = item.isSelected
            checkBox.text = item.title
            checkBox.visibility = VISIBLE
        }
    }
}
