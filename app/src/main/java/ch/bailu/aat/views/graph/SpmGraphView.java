package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;

import ch.bailu.aat.description.CadenceDescription;
import ch.bailu.aat.description.HeartRateDescription;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.attributes.SampleRate;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;

public class SpmGraphView extends AbsGraphView {

    public SpmGraphView(Context context, DispatcherInterface di, int... iid) {
        super(context, di, iid);
        setLabelText();
    }


    public SpmGraphView(Context context) {
        super(context);
        setLabelText();
    }


    private void setLabelText() {
        ylabel.setText(AppTheme.COLOR_BLUE, HeartRateDescription.LABEL, HeartRateDescription.UNIT);
        ylabel.setText(AppTheme.COLOR_GREEN, CadenceDescription.LABEL, CadenceDescription.UNIT);
    }


    @Override
    public void plot(Canvas canvas, GpxList list, int index, SolidUnit sunit, boolean markerMode) {


        int km_factor = (int) (list.getDelta().getDistance()/1000) + 1;

        GraphPlotter plotterHr = new GraphPlotter(canvas,getWidth(), getHeight(), 1000 * km_factor,
                new AppDensity(getContext()));

        GraphPlotter plotterCadence = new GraphPlotter(canvas,getWidth(), getHeight(), 1000 * km_factor,
                new AppDensity(getContext()));

        int max = Math.max(
                list.getDelta().getAttributes().getAsInteger(SampleRate.Cadence.INDEX_MAX_CADENCE),
                list.getDelta().getAttributes().getAsInteger(SampleRate.HeartRate.INDEX_MAX_HR));



        plotterHr.inlcudeInYScale(max);
        plotterHr.inlcudeInYScale(25);

        plotterCadence.inlcudeInYScale(max);
        plotterCadence.inlcudeInYScale(25);

        final GpxListWalker hrPainter =
                    new GraphPainter(plotterHr, (int)list.getDelta().getDistance() / getWidth(),
                            AppTheme.COLOR_BLUE, SampleRate.HeartRate.GPX_KEYS);

        final GpxListWalker cadencePainter =
                new GraphPainter(plotterCadence, (int)list.getDelta().getDistance() / getWidth(),
                        AppTheme.COLOR_GREEN, SampleRate.Cadence.GPX_KEYS);



        plotterHr.roundYScale(25);
        plotterCadence.roundYScale(25);


        if (max > 0) {
            hrPainter.walkTrack(list);
            cadencePainter.walkTrack(list);
        }


        plotterHr.drawXScale(5, sunit.getDistanceFactor());
        plotterHr.drawYScale(5, sunit.getAltitudeFactor(), true);

    }



    private static class GraphPainter extends GpxListWalker {
        private final GraphPlotter plotter;

        private float distance=0;
        private float summaryDistance=0;
        private final float minDistance;

        private final int[] keys;
        private final int color;

        public GraphPainter(GraphPlotter p, int md, int c, int... k) {
            plotter=p;
            minDistance=md*SAMPLE_WIDTH_PIXEL;
            keys = k;
            color = c;
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
                final int value = SampleRate.getValue(point.getAttributes(), keys);

                if (value > 0) {
                    distance += summaryDistance;
                    summaryDistance = 0;

                    plotter.plotData(distance, value, color);
                }
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
}
