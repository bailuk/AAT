package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.view.VerticalScrollView
import ch.bailu.aat_gtk.view.graph.GraphView
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
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.filter.SelectFilter
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.view.graph.DistanceAltitudePlotter

class DetailView(dispatcher: DispatcherInterface, usageTracker: UsageTrackerInterface, storage: StorageInterface) : VerticalScrollView() {

    init {
        val graphView = GraphView(DistanceAltitudePlotter(SolidUnit(storage)))
        graphView.height = 100
        dispatcher.addTarget(SelectFilter(graphView, usageTracker), *InformationUtil.getMapOverlayInfoIdList().toIntArray())

        graphView.overlay.addCssClass("graph-view")
        add(graphView.overlay)
        addAllContent(dispatcher, getSummaryData(storage), usageTracker, *InformationUtil.getMapOverlayInfoIdList().toIntArray())
    }

    private fun getSummaryData(storage: StorageInterface): Array<ContentDescription> {
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
