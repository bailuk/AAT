package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.util.color.ColorInterface;

public class DistanceAltitudePlotter extends Plotter {

    private final SolidUnit sunit;
    private final Segment segment = new Segment();

    public DistanceAltitudePlotter(SolidUnit sunit) {
        this.sunit = sunit;
    }

    @Override
    public void plot(GraphCanvas canvas, PlotterConfig config) {

        boolean markerMode = config.getList().getMarkerList().size() > config.getWidth() / Config.SAMPLE_WIDTH_PIXEL;

        DistanceWalker distances = new DistanceWalker(segment);
        distances.walkTrack(config.getList());

        int km_factor = (int) (distances.getDistanceDelta()/1000) + 1;

        GraphPlotter plotter = new GraphPlotter(canvas, config.getWidth(), config.getHeight(), 1000 * km_factor);

        GpxListWalker painter, scaleGenerator;

        int minDistance = (int)distances.getDistanceDelta() / config.getWidth();
        if (segment.isValid()) {
            painter = new GraphPainterLimit(plotter, segment, minDistance);
            scaleGenerator = new ScaleGeneratorSegmented(plotter, segment);

        } else if (markerMode) {
            painter = new GraphPainterMarkerMode(plotter, minDistance);
            scaleGenerator = new ScaleGeneratorMarkerMode(plotter);
        } else {
            painter = new GraphPainter(plotter, minDistance);
            scaleGenerator = new ScaleGenerator(plotter);

        }

        scaleGenerator.walkTrack(config.getList());
        plotter.roundYScale(50);


        painter.walkTrack(config.getList());


        new SegmentNodePainter(plotter, distances.getDistanceOffset()).walkTrack(config.getList());
        if (config.getIndex() > -1) {
            new IndexPainter(plotter, config.getIndex(), distances.getDistanceOffset()).walkTrack(config.getList());
        }

        plotter.drawXScale(5, sunit.getDistanceFactor(), config.isXLabelVisible());
        plotter.drawYScale(5, sunit.getAltitudeFactor(), true);

    }

    @Override
    public void initLabels(LabelInterface labels) {
        labels.setText(ColorInterface.WHITE, Res.str().altitude(), sunit.getAltitudeUnit());
    }

    @Override
    public void setLimit(int firstPoint, int lastPoint) {
        segment.setLimit(firstPoint, lastPoint);
    }
}
