package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.AltitudeDelta;
import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidPostprocessedAutopause;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.preferences.SolidUnit;


public class DistanceSpeedGraphView extends AbsGraphView {

    public DistanceSpeedGraphView(Context context, DispatcherInterface di, int iid) {
        super(context, di, iid);
    }


    
    @Override
    public void plot(Canvas canvas, GpxList list, SolidUnit sunit, boolean markerMode) {
        int km_factor = (int) (list.getDelta().getDistance()/1000) + 1;

        GraphPlotter plotter[] = new GraphPlotter[3];

        for (int i=0; i<plotter.length; i++) {
            plotter[i] = new GraphPlotter(canvas,getWidth(), getHeight(), 1000 * km_factor,
                    new AppDensity(getContext()));
        }


        for(GraphPlotter p: plotter) {
            p.inlcudeInYScale(list.getDelta().getMaximumSpeed());
            p.inlcudeInYScale(0f);
        }

        


        float meter_pixel = list.getDelta().getDistance()/getWidth();
        
        if (markerMode) {
            new GraphPainterMarkerMode(plotter, meter_pixel).walkTrack(list);
        } else {
            new GraphPainter(plotter, meter_pixel).walkTrack(list);
        }

        plotter[0].drawXScale(5,
                plotterLabel(R.string.distance, sunit.getDistanceUnit()),
                sunit.getDistanceFactor());

        plotter[0].drawYScale(5,
                plotterLabel(R.string.speed, sunit.getSpeedUnit()),
                sunit.getSpeedFactor(), false);

    }



    private class GraphPainter extends GpxListWalker {

        private final GraphPlotter[] plotter;

        private float totalDistance=0;
        private long totalTime=0;
        
        private float distanceOfSample=0;
        private long timeOfSample=0;

        SolidAutopause spause = new SolidPostprocessedAutopause(getContext());

        AutoPause autoPause = new AutoPause.Time(
                        spause.getTriggerSpeed(),
                        spause.getTriggerLevelMillis());

        private final float minDistance;
        
       
        
        public GraphPainter(GraphPlotter[] p, float md) {
            plotter=p;
            minDistance=md*SAMPLE_WIDTH_PIXEL;
        }


        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }

        @Override
        public void doPoint(GpxPointNode point) {
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
            }
        }


        private void plotAverage() {
            if (timeOfSample > 0) {
                float avg=distanceOfSample/timeOfSample*1000;
                plotter[0].plotData(totalDistance, avg, AppTheme.getHighlightColor());
            }
            timeOfSample=0; 
            distanceOfSample=0;
        }

        private void plotTotalAverage() {
            long timeDelta = totalTime - autoPause.get();

            if (timeDelta > 0) {
                float avg = totalDistance / totalTime * 1000;
                plotter[1].plotData(totalDistance, avg, AppTheme.getHighlightColor2());

                float avgAp=totalDistance/timeDelta*1000;
                plotter[2].plotData(totalDistance, avgAp, AppTheme.getHighlightColor3());

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



    private class GraphPainterMarkerMode extends GraphPainter {

        public GraphPainterMarkerMode(GraphPlotter[] p, float md) {
            super(p,md);
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            plotIfDistance();
            marker.getAutoPause();
            increment(marker.getDistance(), marker.getTimeDelta());

            return false;
        }
    }        
}
