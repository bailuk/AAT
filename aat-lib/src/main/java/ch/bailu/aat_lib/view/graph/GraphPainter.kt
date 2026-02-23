package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.lib.color.AltitudeColorTable;

public class GraphPainter extends GpxListWalker {
    private final GraphPlotter plotter;

    private float distance=0;
    private float summaryDistance=0;
    private final float minDistance;


    public GraphPainter(GraphPlotter p, int md) {
        plotter=p;
        minDistance=md * Config.SAMPLE_WIDTH_PIXEL;
    }



    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return true;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        incrementSummaryDistance(point.getDistance());
        plotIfDistance(point);
    }


    public void incrementSummaryDistance(float distance) {
        summaryDistance += distance;
    }

    public void plotIfDistance(GpxPointNode point) {
        if (summaryDistance >= minDistance) {
            int altitude = (int)point.getAltitude();

            distance+=summaryDistance;
            summaryDistance=0;

            plotter.plotData(distance, altitude, AltitudeColorTable.instance().getColor(altitude));
        }
    }



    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return true;
    }

    @Override
    public boolean doList(GpxList track) {
        return true;
    }

}
