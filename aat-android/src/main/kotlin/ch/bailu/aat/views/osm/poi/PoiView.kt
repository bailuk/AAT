package ch.bailu.aat.views.osm.poi

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.osm.EditTextTool
import ch.bailu.aat.views.osm.features.OnSelected
import ch.bailu.aat.views.preferences.SolidStringView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.lib.filter_list.AbsListItem
import ch.bailu.aat_lib.lib.filter_list.FilterList
import ch.bailu.aat_lib.lib.filter_list.FilterListUtil
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.search.poi.PoiListItem
import ch.bailu.foc.Foc
import org.mapsforge.poi.storage.PoiCategory

class PoiView(
    context: Context,
    private val appContext: AppContext,
    private val selected: Foc,
    theme: UiTheme
) : LinearLayout(context), OnPreferencesChanged {

    companion object {
        private const val FILTER_KEY = "PoiView"
    }

    private val filterView: EditText = EditText(context).apply {
        isSingleLine = true
        setText(SolidString(Storage(context), FILTER_KEY).getValueAsStringNonDef())
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                filterList(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })

    }
    private val filterList = FilterList()

    private val listView = PoiListView(context, filterList, theme).apply {
        setOnTextSelected { item: AbsListItem, _: OnSelected.Action?, _: String? ->
            if (item.isSummary()) {
                filterView.setText(item.getSummaryKey())
            } else {
                item.setSelected(!item.isSelected())
                filterList.filterAll()
                onChanged()
            }
        }
    }

    private val sdatabase: SolidPoiDatabase = SolidPoiDatabase(appContext.mapDirectory, appContext)

    init {
        sdatabase.register(this)
        orientation = VERTICAL
        addView(createHeader(theme))
        addView(EditTextTool(filterView, VERTICAL, theme))
        addView(listView)
        readList(appContext)
        filterList(filterView.text.toString())
    }

    private fun readList(appContext: AppContext) {
        FilterListUtil.readList(filterList, appContext, sdatabase.getValueAsString(), selected)
        listView.onChanged()
    }

    private fun createHeader(theme: UiTheme): View {
        return SolidStringView(context, sdatabase, theme)
    }



    private fun filterList(string: String) {
        filterList.filter(string)
        listView.onChanged()
    }

    fun close(sc: ServiceContext) {
        saveSelected()
        SolidString(Storage(sc.getContext()), FILTER_KEY).setValue(
            filterView.text.toString()
        )
        sdatabase.unregister(this)
    }

    val selectedCategories: ArrayList<PoiCategory>
        get() {
            val export = ArrayList<PoiCategory>(10)
            for (i in 0 until filterList.sizeVisible()) {
                val item = filterList.getFromVisible(i)

                if (item.isSelected() && item is PoiListItem) {
                    export.add(item.category)
                }
            }
            return export
        }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sdatabase.hasKey(key)) {
            saveSelected()
            readList(appContext)
        }
    }

    private fun saveSelected() {
        saveSelected(selected)
    }

    fun saveSelected(file: Foc) {
        try {
            FilterListUtil.writeSelected(filterList, file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
