package ch.bailu.aat.views.list

import android.content.res.Configuration
import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.activities.AbsGpxListActivity
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.map.layer.FileControlBarLayer
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.busy.BusyViewControlDbSync
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.preferences.SolidCheckBox
import ch.bailu.aat.views.preferences.SolidIndexListView
import ch.bailu.aat.views.preferences.TitleView
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.description.PathDescription
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo

class GpxListActivityContentView(private val acontext: AbsGpxListActivity, sdirectory: SolidDirectoryQuery, theme: UiTheme, filterTheme: UiTheme) {
    private val summaryLabel = Res.str().label_summary()
    private val filterLabel = Res.str().label_filter()
    private val mapLabel = Res.str().intro_map()
    private val listLabel = Res.str().label_list()
    private val solidKey = "${AbsGpxListActivity::class.java.simpleName}_${sdirectory.getValueAsString()}"

    private val map: MapViewInterface = MapFactory.createDefaultMapView(acontext, solidKey).list()
    private val listLayout = LinearLayout(acontext).apply {
        orientation = LinearLayout.VERTICAL
        theme.background(this)
    }

    val contentView = ContentView(acontext, theme)
    val busyControl = BusyViewControlDbSync(contentView)
    val listFilterView = GpxListFilterView(acontext, acontext.dispatcher, sdirectory, theme)
    val listView = GpxListView(acontext, acontext.gpxListItemData).apply {
        onItemClickListener = acontext
    }
    val fileControlBar = FileControlBarLayer(acontext.appContext, map.getMContext(), acontext, acontext.appContext.summaryConfig).apply {
        map.add(this)
    }

    init {
        listLayout.addView(listFilterView.layout)
        listLayout.addView(listView)
        acontext.registerForContextMenu(listView)

        val summary = VerticalScrollView(acontext)
        summary.add(TitleView(acontext, acontext.label, theme))
        summary.add(acontext.dispatcher, PathDescription(), theme, InfoID.LIST_SUMMARY)
        summary.add(TitleView(acontext, summaryLabel, theme))

        summary.addAllContent(acontext.dispatcher, acontext.summaryData, theme, InfoID.LIST_SUMMARY)
        summary.add(TitleView(acontext, ToDo.translate("Sort"), theme))
        summary.add(SolidIndexListView(acontext, sdirectory.solidSortAttribute, theme))
        summary.add(SolidCheckBox(acontext, sdirectory.solidSortOrderAscend, theme))
        summary.add(TitleView(acontext, filterLabel, filterTheme))
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

        multiView.add(listLayout, listLabel)
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
            a.add(listLayout, 30)
            a.add(summary, 30)
            a.add(To.view(map)!!, 40)
            a
        } else {
            val a = PercentageLayout(acontext)
            a.setOrientation(LinearLayout.HORIZONTAL)
            a.add(listLayout, 50)
            a.add(summary, 50)
            val b = PercentageLayout(acontext)
            b.add(a, 60)
            b.add(To.view(map)!!, 40)
            b
        }
    }
}
