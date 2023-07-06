package ch.bailu.aat.activities

import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.NameDescription
import ch.bailu.aat_lib.description.PathDescription
import ch.bailu.aat_lib.description.TrackSizeDescription
import ch.bailu.aat_lib.gpx.InfoID

class GpxEditorActivity : AbsFileContentActivity() {
    companion object {
        private const val SOLID_KEY = "gpx_editor"
    }

    override fun createLayout(bar: MainControlBar, contentView: ContentView): ViewGroup {
        map = MapFactory.DEF(this, SOLID_KEY).editor(editorSource)
        val summaryData = arrayOf(
            NameDescription(),
            PathDescription(),
            DistanceDescription(appContext.storage),
            TrackSizeDescription()
        )
        val summary = VerticalScrollView(this)
        summary.addAllContent(
            this, summaryData,
            AppTheme.trackContent,
            InfoID.EDITOR_OVERLAY,
            InfoID.FILEVIEW
        )
        summary.add(createAttributesView())
        val graph = GraphViewFactory.createAltitudeGraph(appContext, this, THEME)
            .connect(this, InfoID.EDITOR_OVERLAY, InfoID.FILEVIEW)
        return if (AppLayout.isTablet(this)) {
            createPercentageLayout(summary, graph)
        } else {
            createMultiView(bar, summary, graph, contentView)
        }
    }

    private fun createMultiView(bar: MainControlBar, summary: View, graph: View, contentView: ContentView): ViewGroup {
        val mv = MultiView(this, SOLID_KEY)
        To.view(map)?.apply { mv.add(this) }
        val p = PercentageLayout(this)
        p.add(summary, 60)
        p.add(graph, 40)
        mv.add(p)
        bar.addMvNext(mv)
        contentView.addMvIndicator(mv)
        return mv
    }

    private fun createPercentageLayout(summary: View, graph: View): ViewGroup {
        return if (AppLayout.getOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            val a = PercentageLayout(this)
            a.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
            a.add(To.view(map)!!, 60)
            a.add(summary, 40)
            val b = PercentageLayout(this)
            b.add(a, 85)
            b.add(graph, 15)
            b
        } else {
            val a = PercentageLayout(this)
            a.setOrientation(LinearLayout.HORIZONTAL)
            a.add(To.view(map)!!, 100)
            val b = PercentageLayout(this)
            b.add(a, 70)
            b.add(summary, 30)
            val c = PercentageLayout(this)
            c.add(b, 85)
            c.add(graph, 15)
            c
        }
    }
}
