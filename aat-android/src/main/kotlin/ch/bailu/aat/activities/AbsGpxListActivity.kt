package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import ch.bailu.aat.map.layer.FileControlBarLayer
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.busy.BusyViewControlDbSync
import ch.bailu.aat.views.list.GpxListActivityContentView
import ch.bailu.aat.views.list.GpxListFilterView
import ch.bailu.aat.views.list.GpxListView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.IteratorSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.service.directory.Iterator
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

abstract class AbsGpxListActivity : ActivityContext(), OnItemClickListener, OnPreferencesChanged {
    private val theme: UiTheme = AppTheme.trackList
    private val filterTheme: UiTheme = AppTheme.filter

    private var iteratorSimple = Iterator.NULL
    private var sdirectory: SolidDirectoryQuery? = null
    private var listView: GpxListView? = null
    private var fileControlBar: FileControlBarLayer? = null
    private var busyControl: BusyViewControlDbSync? = null
    private var listFilterView: GpxListFilterView? = null

    abstract fun displayFile()

    abstract val label: String

    abstract val directory: Foc
    abstract val gpxListItemData: Array<ContentDescription>
    abstract val summaryData: Array<ContentDescription>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sdirectory = SolidDirectoryQuery(Storage(this), FocAndroidFactory(this)).apply {
            setValue(directory.path)
        }
        AbsGpxListActivity@this.sdirectory = sdirectory

        val contentView = GpxListActivityContentView(this, sdirectory, theme, filterTheme)
        busyControl = contentView.busyControl
        listView = contentView.listView
        fileControlBar = contentView.fileControlBar
        listFilterView = contentView.listFilterView

        setContentView(contentView.contentView)
        createDispatcher(contentView.busyControl)

        // Disable keyboard
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    private fun createDispatcher(busyControl: TargetInterface) {
        dispatcher.addSource(IteratorSource.Summary(appContext))
        dispatcher.addOverlaySources(appContext, UsageTrackers().createOverlayUsageTracker(appContext.storage, *InformationUtil.getOverlayInfoIdList().toIntArray()))
        dispatcher.addSource(CurrentLocationSource(appContext.services, appContext.broadcaster))
        dispatcher.addTarget(busyControl, *InformationUtil.getOverlayInfoIdList().toIntArray())
    }

    override fun onResumeWithService() {
        iteratorSimple = IteratorSimple(appContext)
        listView?.setIterator(this, iteratorSimple)
        fileControlBar?.setIterator(iteratorSimple)
        sdirectory?.apply {
            listView?.setSelection(position.getValue())
            register(this@AbsGpxListActivity)
            setListBackgroundColor(isFilterEnabled())
        }

        super.onResumeWithService()
    }

    override fun onPauseWithService() {
        iteratorSimple.close()
        iteratorSimple = Iterator.NULL
        listView?.setIterator(this, iteratorSimple)
        fileControlBar?.setIterator(iteratorSimple)
        sdirectory?.unregister(this)
        super.onPauseWithService()
    }

    override fun onDestroy() {
        busyControl?.close()
        super.onDestroy()
    }

    override fun onItemClick(arg0: AdapterView<*>?, arg1: View, position: Int, arg3: Long) {
        displayFileOnPosition(position)
    }

    private fun displayFileOnPosition(position: Int) {
        sdirectory?.position?.setValue(position)
        displayFile()
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        sdirectory?.apply {
            if (containsKey(key)) {
                setListBackgroundColor(isFilterEnabled())
            }
        }
    }

    private fun setListBackgroundColor(hasFilter: Boolean) {
        if (hasFilter) {
            listView?.themify(filterTheme)
            listFilterView?.themify(filterTheme)
        } else {
            listView?.themify(theme)
            listFilterView?.themify(theme)
        }
    }
}
