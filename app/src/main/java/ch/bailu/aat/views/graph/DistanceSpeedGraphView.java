package ch.bailu.aat.views.graph;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.GpxWindow;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat.preferences.SolidSpeedGraphWindow;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;


public class DistanceSpeedGraphView extends AbsGraphView implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final SolidSpeedGraphWindow swindow;

    public DistanceSpeedGraphView(Context context, String key, DispatcherInterface di, int... iid) {
        super(context, di, iid);

        swindow = new SolidSpeedGraphWindow(context, key);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        swindow.register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        swindow.unregister(this);
    }

    @Override
    public void plot(Canvas canvas, GpxList list, int index, SolidUnit sunit, boolean markerMode) {
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



        new GraphPainter(plotter, meter_pixel).walkTrack(list);

        plotter[0].drawXScale(5,
                plotterLabel(R.string.distance, sunit.getDistanceUnit()),
                sunit.getDistanceFactor());

        plotter[0].drawYScale(5,
                plotterLabel(R.string.speed, sunit.getSpeedUnit()),
                sunit.getSpeedFactor(), false);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (swindow.hasKey(key)) {
            invalidate();
        }
    }


    private class GraphPainter extends GpxListWalker {

        private final GraphPlotter[] plotter;

        private float totalDistance=0;
        private long totalTime=0;

        private float distanceOfSample=0;
        private long timeOfSample=0;

        final SolidAutopause spause = new SolidPostprocessedAutopause(getContext());

        final AutoPause autoPause = new AutoPause.Time(
                spause.getTriggerSpeed(),
                spause.getTriggerLevelMillis());

        private final float minDistance;


        private GpxWindow window;



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
                plotter[0].plotData(totalDistance, avg, AppTheme.getHighlightColor());
            }
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
            window = swindow.createWindow((GpxPointNode) track.getPointList().getFirst());
            return true;
        }
    }



}
