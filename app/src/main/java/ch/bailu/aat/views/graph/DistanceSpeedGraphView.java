package ch.bailu.aat.views.graph;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.AverageSpeedDescriptionAP;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListAttributes;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.GpxWindow;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidSpeedGraphWindow;
import ch.bailu.aat.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.bar.ControlBar;


public class DistanceSpeedGraphView extends AbsGraphView implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final SolidSpeedGraphWindow swindow;
    private final ControlBar bar;


    public DistanceSpeedGraphView(Context context, String key, DispatcherInterface di, int... iid) {
        super(context, di, iid);

        swindow = new SolidSpeedGraphWindow(context, key);

        setLabelText(context);

        bar = new ControlBar(context, LinearLayout.HORIZONTAL,6);
        bar.addSolidIndexButton(new SolidSpeedGraphWindow(context, key));
        bar.setBackgroundColor(Color.TRANSPARENT);
        bar.setGravity(Gravity.RIGHT);
        addView(bar);
    }

    private void setLabelText(Context context) {
        ylabel.setText(Color.WHITE, R.string.speed, sunit.getSpeedUnit());
        ylabel.setText(AppTheme.COLOR_BLUE, new AverageSpeedDescriptionAP(context).getLabel());
        ylabel.setText(AppTheme.COLOR_GREEN, new AverageSpeedDescription(context).getLabel());
        ylabel.setText(AppTheme.COLOR_ORANGE, swindow.getValueAsString());
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
            p.inlcudeInYScale(list.getDelta().getAttributes().getAsFloat((GpxListAttributes.INDEX_MAX_SPEED)));
            p.inlcudeInYScale(0f);
        }




        float meter_pixel = list.getDelta().getDistance()/getWidth();

        new GraphPainter(plotter, meter_pixel).walkTrack(list);

        plotter[0].drawXScale(5, sunit.getDistanceFactor());
        plotter[0].drawYScale(5, sunit.getSpeedFactor(), false);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (swindow.hasKey(key)) {
            setLabelText(swindow.getContext());
            invalidate();
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bar.place(0,b-t-bar.getControlSize(), r-l);
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
                plotter[1].plotData(totalDistance, avg, AppTheme.COLOR_GREEN);

                float avgAp=totalDistance/timeDelta * 1000;
                plotter[2].plotData(totalDistance, avgAp, AppTheme.COLOR_BLUE);

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
