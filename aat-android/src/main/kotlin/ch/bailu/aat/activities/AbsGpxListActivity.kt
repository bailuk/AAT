package ch.bailu.aat.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.map.layer.FileControlBarLayer
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.busy.BusyViewControlDbSync
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.list.GpxListView
import ch.bailu.aat.views.preferences.TitleView
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.PathDescription
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.IteratorSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.directory.Iterator
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

abstract class AbsGpxListActivity : ActivityContext(), OnItemClickListener, OnPreferencesChanged {
    private val theme: UiTheme = AppTheme.trackList
    private val filterTheme: UiTheme = AppTheme.filter

    private var solidKey: String = ""
    private var iteratorSimple = Iterator.NULL
    private var sdirectory: SolidDirectoryQuery? = null
    private var listView: GpxListView? = null
    private var fileControlBar: FileControlBarLayer? = null
    private var busyControl: BusyViewControlDbSync? = null

    abstract fun displayFile()

    abstract val label: String

    abstract val directory: Foc
    abstract val gpxListItemData: Array<ContentDescription>
    abstract val summaryData: Array<ContentDescription>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdirectory = SolidDirectoryQuery(Storage(this), FocAndroidFactory(this)).apply {
            setValue(directory.path)
            solidKey = "${AbsGpxListActivity::class.java.simpleName}_${this.getValueAsString()}"
        }

        setContentView(Layouter().contentView)
        createDispatcher()
    }

    private fun createDispatcher() {
        dispatcher.addSource(IteratorSource.Summary(appContext))
        dispatcher.addOverlaySources(appContext, UsageTrackers().createOverlayUsageTracker(appContext.storage, *InformationUtil.getOverlayInfoIdList().toIntArray()))
        dispatcher.addSource(CurrentLocationSource(appContext.services, appContext.broadcaster))

        val busyControl = busyControl

        if (busyControl is TargetInterface) {
            dispatcher.addTarget(busyControl, *InformationUtil.getOverlayInfoIdList().toIntArray())
        }
    }

    override fun onResumeWithService() {
        iteratorSimple = IteratorSimple(appContext)
        listView?.setIterator(this, iteratorSimple)
        fileControlBar?.setIterator(iteratorSimple)
        sdirectory?.apply {
            listView?.setSelection(position.getValue())
            register(this@AbsGpxListActivity)
            setListBackgroundColor(createSelectionString().isNotEmpty())
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
                setListBackgroundColor(createSelectionString().isNotEmpty())
            }
        }
    }

    private fun setListBackgroundColor(hasFilter: Boolean) {
        if (hasFilter) {
            listView?.themify(filterTheme)
        } else {
            listView?.themify(theme)
        }
    }

    private inner class Layouter {
        private val acontext = this@AbsGpxListActivity
        private val summaryLabel = getString(R.string.label_summary)
        private val filterLabel = getString(R.string.label_filter)
        private val mapLabel = getString(R.string.intro_map)
        private val listLabel = getString(R.string.label_list)

        val contentView = ContentView(acontext, theme)

        init {
            listView = GpxListView(
                acontext,
                gpxListItemData
            ).apply {
                onItemClickListener = acontext
            }

            registerForContextMenu(listView)
            busyControl = BusyViewControlDbSync(contentView)
            val map: MapViewInterface = MapFactory.createDefaultMapView(acontext, solidKey).list()

            fileControlBar = FileControlBarLayer(appContext, map.getMContext(), acontext, appContext.summaryConfig).apply {
                map.add(this)
            }


            val summary = VerticalScrollView(acontext)
            summary.add(TitleView(acontext, label, theme))
            summary.add(dispatcher, PathDescription(), theme, InfoID.LIST_SUMMARY)
            summary.add(TitleView(acontext, summaryLabel, theme))
            summary.addAllContent(dispatcher, summaryData, theme, InfoID.LIST_SUMMARY)
            val title = TitleView(acontext, filterLabel, filterTheme)
            summary.add(title)
            summary.addAllFilterViews(map.getMContext(), filterTheme)
            val bar = MainControlBar(acontext)
            val layout = createLayout(map, summary, bar)
            contentView.add(bar)
            contentView.add(layout)
        }

        private fun createLayout(map: MapViewInterface, summary: VerticalScrollView, bar: MainControlBar): View {
            return if (AppLayout.isTablet(acontext)) {
                createTabletLayout(map, summary)
            } else {
                createMvLayout(map, summary, bar)
            }
        }

        private fun createMvLayout(map: MapViewInterface, summary: VerticalScrollView, bar: MainControlBar): View {
            val multiView = MultiView(acontext, solidKey)

            multiView.add(listView!!, listLabel)
            To.view(map)?.apply { multiView.add(this, mapLabel) }
            multiView.add(summary, "$summaryLabel/$filterLabel")
            bar.addAll(multiView)
            contentView.addMvIndicator(multiView)
            return multiView
        }

        private fun createTabletLayout(map: MapViewInterface, summary: VerticalScrollView): View {
             return if (AppLayout.getOrientation(acontext) == Configuration.ORIENTATION_LANDSCAPE) {
                val a = PercentageLayout(acontext)
                a.setOrientation(LinearLayout.HORIZONTAL)
                a.add(listView!!, 30)
                a.add(summary, 30)
                a.add(To.view(map)!!, 40)
                a
            } else {
                val a = PercentageLayout(acontext)
                a.setOrientation(LinearLayout.HORIZONTAL)
                a.add(listView!!, 50)
                a.add(summary, 50)
                val b = PercentageLayout(acontext)
                b.add(a, 60)
                b.add(To.view(map)!!, 40)
                b
            }
        }
    }
}
