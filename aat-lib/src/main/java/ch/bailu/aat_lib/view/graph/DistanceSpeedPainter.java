package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.app.AppColor;
import ch.bailu.aat_lib.gpx.GpxDistanceWindow;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;

public class DistanceSpeedPainter extends GpxListWalker {

    private final float minDistance;
    private final GpxDistanceWindow window;
    private final AutoPause autoPause;
    private final GraphPlotter[] plotter;

    private float totalDistance=0;
    private long totalTime=0;

    private float distanceOfSample=0;
    private long timeOfSample=0;


    public DistanceSpeedPainter(GraphPlotter[] plotter,
                        AutoPause autoPause,
                        float minDistance,
                        GpxDistanceWindow window) {
        this.plotter = plotter;
        this.autoPause = autoPause;
        this.minDistance = minDistance;
        this.window = window;
    }



    @Override
    public boolean doList(GpxList track) {
        return true;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return true;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        window.forward(point);
        autoPause.update(point);
        increment(point.getDistance(), point.getTimeDelta());
        plotIfDistance();
    }

    public void increment(float distance, long time) {
        distanceOfSample += distance;
        timeOfSample += time;
    }


    public void plotIfDistance() {
        if (distanceOfSample >= minDistance) {
            totalTime+=timeOfSample;
            totalDistance += distanceOfSample;

            plotTotalAverage();
            plotAverage();

            timeOfSample=0;
            distanceOfSample=0;
        }
    }


    private void plotAverage() {
        if (window.getTimeDelta() > 0) {
            float avg=window.getSpeed();
            plotter[0].plotData(totalDistance, avg, AppColor.HL_ORANGE);
        }
    }


    private void plotTotalAverage() {
        long timeDelta = totalTime - autoPause.getPauseTime();

        if (timeDelta > 0) {
            float avg = totalDistance / totalTime * 1000;
            plotter[1].plotData(totalDistance, avg, AppColor.HL_GREEN);

            float avgAp=totalDistance / timeDelta * 1000;
            plotter[2].plotData(totalDistance, avgAp, AppColor.HL_BLUE);

        }
    }


    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return true;
    }
}