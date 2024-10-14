package ch.bailu.aat.views.osm.features

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.SolidOsmFeaturesList
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.cache.osm_features.ObjMapFeatures
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.busy.BusyIndicator
import ch.bailu.aat.views.osm.EditTextTool
import ch.bailu.aat.views.preferences.SolidCheckBox
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.lib.filter_list.FilterList
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface

class OsmFeaturesView(private val scontext: ServiceContext) : LinearLayout(
    scontext.getContext()
), OnPreferencesChanged {
    private val filterView: EditText

    private var listHandle: ObjMapFeatures? = null

    private val busy = BusyIndicator(context)
    private val slist = SolidOsmFeaturesList(context)
    private val list = FilterList()
    private val listView = MapFeaturesListView(scontext, list)

    private val theme = AppTheme.search
    private val onListLoaded: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val handle = listHandle
            if (handle != null && AppIntent.hasFile(intent, handle.getID())) {
                updateList()
            }
        }
    }

    companion object {
        private const val FILTER_KEY = "FilterView"
    }

    init {
        orientation = VERTICAL
        filterView = EditText(context)
        filterView.isSingleLine = true
        filterView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                list.filter(charSequence.toString())
                updateList()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        val layout = EditTextTool(filterView, VERTICAL, theme)
        layout.add(SolidCheckBox(context, slist, theme))

        addView(layout)
        addView(listView)
    }

    private fun updateList() {
        val handle = this.listHandle

        if (handle == null) {
            list.clear()
            busy.stopWaiting()
        } else {
            handle.syncList(list)
            if (handle.isReadyAndLoaded()) busy.stopWaiting() else busy.startWaiting()
        }
        listView.onChanged()
    }

    fun setOnTextSelected(s: OnSelected) {
        listView.setOnTextSelected(s)
    }


    fun onResume(sc: ServiceContext) {
        AndroidBroadcaster.register(
            sc.getContext(),
            onListLoaded,
            AppBroadcaster.FILE_CHANGED_INCACHE
        )
        filterView.setText(SolidString(Storage(context), FILTER_KEY).getValueAsStringNonDef())
        getListHandle()
        slist.register(this)
    }

    fun onPause(sc: ServiceContext) {
        slist.unregister(this)
        sc.getContext().unregisterReceiver(onListLoaded)
        freeListHandle()
        SolidString(Storage(sc.getContext()), FILTER_KEY).setValue(filterView.text.toString())
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (slist.hasKey(key)) {
            freeListHandle()
            getListHandle()
        }
    }

    private fun getListHandle() {
        scontext.insideContext {
            listHandle = slist.getList(scontext.getCacheService())
        }
        updateList()
    }

    private fun freeListHandle() {
        listHandle?.free()
        listHandle = null
        updateList()
    }

    fun setFilterText(summaryKey: String) {
        filterView.setText(summaryKey)
    }
}
