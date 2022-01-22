package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_lib.description.*
import ch.bailu.aat_lib.description.IndexedAttributeDescription.HeartBeats
import ch.bailu.aat_lib.description.IndexedAttributeDescription.TotalCadence
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.StorageInterface

class GpxDetailView(di: DispatcherInterface, storage: StorageInterface) : VerticalScrollView() {
    init {
        addAllContent(di, getSummaryData(storage), InfoID.FILEVIEW)
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