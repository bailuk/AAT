package ch.bailu.aat.views.graph;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.description.AverageSpeedDescription;
import ch.bailu.aat_lib.description.AverageSpeedDescriptionAP;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.GpxDistanceWindow;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed;
import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;


public class DistanceSpeedGraphView extends AbsGraphView implements SharedPreferences.OnSharedPreferenceChangeListener {


    public DistanceSpeedGraphView(Context context, DispatcherInterface di, UiTheme theme, int... iid) {
        super(context, di, theme, iid);
        setLabelText(context);
    }


    private void setLabelText(Context context) {
        ylabel.setText(Color.WHITE, R.string.speed, sunit.getSpeedUnit());
        ylabel.setText(AppTheme.HL_BLUE, new AverageSpeedDescriptionAP(new Storage(context)).getLabel());
        ylabel.setText(AppTheme.HL_GREEN, new AverageSpeedDescription(new Storage(context)).getLabel());
    }


    @Override
    public void plot(Canvas canvas, GpxList list, int index, SolidUnit sunit, boolean markerMode) {
        GraphPlotter[] plotter = initPlotter(canvas, list);
        GpxDistanceWindow window = new GpxDistanceWindow(list);

        ylabel.setText(AppTheme.HL_ORANGE,
                window.getLimitAsString(getContext()));

        new GraphPainter(plotter, getAutoPause(), getMinDistance(list), window)
                .walkTrack(list);

        plotter[0].drawXScale(5, sunit.getDistanceFactor(), isXLabelVisible());
        plotter[0].drawYScale(5, sunit.getSpeedFactor(), false);

    }

    private float getMinDistance(GpxList list) {
        float distForOnePixel = list.getDelta().getDistance()/getWidth();
        return distForOnePixel * SAMPLE_WIDTH_PIXEL;
    }


    private AutoPause getAutoPause() {
        int preset = new SolidPreset(new Storage(getContext())).getIndex();
        final SolidAutopause spause = new SolidPostprocessedAutopause(new Storage(getContext()), preset);
        return new AutoPause.Time(
                spause.getTriggerSpeed(),
                spause.getTriggerLevelMillis());
    }

    private GraphPlotter[] initPlotter(Canvas canvas, GpxList list) {
        int km_factor = (int) (list.getDelta().getDistance()/1000) + 1;
        int xscale = km_factor * 1000;

        float maxSpeed = list.getDelta().getAttributes().getAsFloat((MaxSpeed.INDEX_MAX_SPEED));
        AndroidAppDensity density = new AndroidAppDensity(getContext());

        return  initPlotter(canvas, xscale, density, maxSpeed);
    }


    private GraphPlotter[] initPlotter(Canvas canvas, int xscale, AndroidAppDensity density, float maxSpeed) {
        GraphPlotter[] result = new GraphPlotter[3];
        for (int i=0; i<result.length; i++) {
            result[i] = new GraphPlotter(canvas, getWidth(), getHeight(), xscale,
                    density, theme);
            result[i].inlcudeInYScale(0f);
            result[i].inlcudeInYScale(maxSpeed);
        }
        return result;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {}


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    private class GraphPainter extends GpxListWalker {

        private final float minDistance;
        private final GpxDistanceWindow window;
        private final AutoPause autoPause;
        private final GraphPlotter[] plotter;

        private float totalDistance=0;
        private long totalTime=0;

        private float distanceOfSample=0;
        private long timeOfSample=0;


        public GraphPainter(GraphPlotter[] plotter,
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
                plotter[0].plotData(totalDistance, avg, AppTheme.HL_ORANGE);
            }
        }


        private void plotTotalAverage() {
            long timeDelta = totalTime - autoPause.getPauseTime();

            if (timeDelta > 0) {
                float avg = totalDistance / totalTime * 1000;
                plotter[1].plotData(totalDistance, avg, AppTheme.HL_GREEN);

                float avgAp=totalDistance / timeDelta * 1000;
                plotter[2].plotData(totalDistance, avgAp, AppTheme.HL_BLUE);

            }
        }


        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return true;
        }
    }
}
