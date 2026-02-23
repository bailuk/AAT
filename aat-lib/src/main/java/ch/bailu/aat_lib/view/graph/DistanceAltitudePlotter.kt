package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.lib.color.ColorInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res.str
import kotlin.math.max

class DistanceAltitudePlotter(private val sunit: SolidUnit) : Plotter() {
    private val segment = Segment()

    override fun plot(canvas: GraphCanvas, config: PlotterConfig) {
        val markerMode =
            config.getList().markerList.size() > config.getWidth() / Config.SAMPLE_WIDTH_PIXEL

        val distances = DistanceWalker(segment)
        distances.walkTrack(config.getList())

        val kmFactor = (distances.distanceDelta / 1000).toInt() + 1

        val plotter = GraphPlotter(
            canvas,
            config.getWidth(),
            config.getHeight(),
            (1000 * kmFactor).toFloat()
        )

        val painter: GpxListWalker?
        val scaleGenerator: GpxListWalker?

        val minDistance = distances.distanceDelta.toInt() / max(config.getWidth(), 1)
        if (segment.isValid()) {
            painter = GraphPainterLimit(plotter, segment, minDistance)
            scaleGenerator = ScaleGeneratorSegmented(plotter, segment)
        } else if (markerMode) {
            painter = GraphPainterMarkerMode(plotter, minDistance)
            scaleGenerator = ScaleGeneratorMarkerMode(plotter)
        } else {
            painter = GraphPainter(plotter, minDistance)
            scaleGenerator = ScaleGenerator(plotter)
        }

        scaleGenerator.walkTrack(config.getList())
        plotter.roundYScale(50)


        painter.walkTrack(config.getList())


        SegmentNodePainter(plotter, distances.distanceOffset).walkTrack(config.getList())
        if (config.getIndex() > -1) {
            IndexPainter(
                plotter,
                config.getIndex(),
                distances.distanceOffset
            ).walkTrack(config.getList())
        }

        plotter.drawXScale(5, sunit.distanceFactor, config.isXLabelVisible())
        plotter.drawYScale(5, sunit.altitudeFactor, true)
    }

    override fun initLabels(labels: LabelInterface) {
        labels.setText(ColorInterface.WHITE, str().altitude(), sunit.altitudeUnit)
    }

    override fun setLimit(firstPoint: Int, lastPoint: Int) {
        segment.setLimit(firstPoint, lastPoint)
    }
}
