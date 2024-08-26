package ch.bailu.aat.activities

import android.content.Context
import android.view.View
import android.view.ViewGroup
import ch.bailu.aat.map.MapFactory
import ch.bailu.aat.map.To
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.layout.PercentageLayout
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.graph.GraphViewFactory
import ch.bailu.aat.views.preferences.VerticalScrollView
import ch.bailu.aat_lib.description.AscendDescription
import ch.bailu.aat_lib.description.AveragePaceDescription
import ch.bailu.aat_lib.description.AveragePaceDescriptionAP
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP
import ch.bailu.aat_lib.description.CaloriesDescription
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.description.ContentDescriptions
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DescendDescription
import ch.bailu.aat_lib.description.DistanceApDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.EndDateDescription
import ch.bailu.aat_lib.description.IndexedAttributeDescription
import ch.bailu.aat_lib.description.IndexedAttributeDescription.HeartBeats
import ch.bailu.aat_lib.description.IndexedAttributeDescription.TotalCadence
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.NameDescription
import ch.bailu.aat_lib.description.PathDescription
import ch.bailu.aat_lib.description.PauseApDescription
import ch.bailu.aat_lib.description.PauseDescription
import ch.bailu.aat_lib.description.TimeApDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.description.TrackSizeDescription
import ch.bailu.aat_lib.gpx.InfoID

class FileContentActivity : AbsFileContentActivity() {

    override fun createLayout(bar: MainControlBar, contentView: ContentView): ViewGroup {
        map = MapFactory.DEF(this, SOLID_KEY).content(editorSource)

        val summary = VerticalScrollView(this)
        summary.addAllContent(
            dispatcher, getSummaryData(this), AppTheme.trackContent,
            InfoID.FILE_VIEW, InfoID.EDITOR_OVERLAY
        )

        summary.add(createAttributesView())

        val graph: View = GraphViewFactory.all(
            appContext, this, dispatcher, THEME,
            InfoID.FILE_VIEW, InfoID.EDITOR_OVERLAY
        )

        return if (AppLayout.isTablet(this)) {
            createPercentageLayout(summary, graph)

        } else {
            createMultiView(bar, summary, graph, contentView)
        }
    }

    private fun createMultiView(bar: MainControlBar, summary: View, graph: View, contentView: ContentView): ViewGroup {
        val mv = MultiView(this, SOLID_KEY)
        mv.add(summary)
        To.view(map)?.apply { mv.add(this) }
        mv.add(graph)
        bar.addMvNext(mv)
        contentView.addMvIndicator(mv)
        return mv
    }

    private fun createPercentageLayout(summary: View, graph: View): ViewGroup {
        val a = PercentageLayout(this)
        a.setOrientation(AppLayout.getOrientationAlongLargeSide(this))
        a.add(To.view(map)!!, 60)
        a.add(summary, 40)

        val b = PercentageLayout(this)
        b.add(a, 70)
        b.add(graph, 30)

        return b
    }

    companion object {
        private const val SOLID_KEY = "file_content"

        fun getSummaryData(context: Context): Array<ContentDescription> {
            val storage = Storage(context)
            return arrayOf(
                NameDescription(),
                PathDescription(),
                DateDescription(),
                EndDateDescription(),
                ContentDescriptions(
                    TimeDescription(),
                    TimeApDescription()
                ),
                ContentDescriptions(
                    PauseDescription(),
                    PauseApDescription()
                ),
                ContentDescriptions(
                    DistanceDescription(storage),
                    DistanceApDescription(storage)
                ),
                ContentDescriptions(
                    AverageSpeedDescription(storage),
                    AverageSpeedDescriptionAP(storage),
                    MaximumSpeedDescription(storage)
                ),
                CaloriesDescription(storage),
                ContentDescriptions(
                    AveragePaceDescription(storage),
                    AveragePaceDescriptionAP(storage)
                ),
                ContentDescriptions(
                    AscendDescription(storage),
                    DescendDescription(storage)
                ),
                ContentDescriptions(
                    IndexedAttributeDescription.HeartRate(),
                    HeartBeats()
                ),
                ContentDescriptions(
                    IndexedAttributeDescription.Cadence(),
                    TotalCadence()
                ),
                TrackSizeDescription()
            )
        }
    }
}
