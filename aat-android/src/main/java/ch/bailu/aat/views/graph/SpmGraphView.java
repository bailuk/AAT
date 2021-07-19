package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;

import ch.bailu.aat.description.CadenceDescription;
import ch.bailu.aat.description.HeartRateDescription;
import ch.bailu.aat.description.StepRateDescription;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.SampleRate;
import ch.bailu.aat_lib.preferences.general.SolidUnit;

public class SpmGraphView extends AbsGraphView {

    final Entry[] entries;

    public SpmGraphView(Context context, DispatcherInterface di, UiTheme theme, int... iid) {
        super(context, di, theme, iid);
        entries = createEntries();
        setLabelText();
    }

    private Entry[] createEntries() {
        return new Entry[]{
                new Entry(AppTheme.HL_BLUE,
                        new HeartRateDescription(),
                        SampleRate.HeartRate.INDEX_MAX_HR,
                        SampleRate.HeartRate.GPX_KEYS),

                new Entry(AppTheme.HL_GREEN,
                        new CadenceDescription(),
                        SampleRate.Cadence.INDEX_MAX_CADENCE,
                        SampleRate.Cadence.GPX_KEYS),


                new Entry(AppTheme.HL_ORANGE,
                        new StepRateDescription(),
                        SampleRate.StepsRate.INDEX_MAX_SPM,
                        SampleRate.StepsRate.GPX_KEYS)
        };
    }


    private void setLabelText() {
        for (Entry e : entries)
            e.setLabelText();
    }



    @Override
    public void plot(Canvas canvas, GpxList list, int index, SolidUnit sunit, boolean markerMode) {

        int max = 0;
        int min = 25;
        int km_factor = (int) (list.getDelta().getDistance()/1000) + 1;

        for (Entry e : entries) {
            e.setPlotter(km_factor, canvas);
            max = Math.max(max, e.getMax(list));
        }

        if (max > 0) {
            for (Entry e: entries) {
                e.getPlotter().inlcudeInYScale(min);
                e.getPlotter().inlcudeInYScale(max);
                e.getPlotter().roundYScale(25);
            }

            new GraphPainter(
                    entries,
                    (int)list.getDelta().getDistance() / getWidth()
            ).walkTrack(list);

            entries[0].getPlotter().drawYScale(5, 1, false);
        }


        entries[0].getPlotter().drawXScale(5, sunit.getDistanceFactor(), isXLabelVisible());
    }



    private class Entry {
        private final int color;
        private final String label;
        private final String unit;

        private final int maxKey;
        private final int[] keys;

        private GraphPlotter plotter;

        private float summaryDistance=0;


        public Entry(int color, ContentDescription description, int maxKey, int... keys) {
            this(color, description.getLabel(), description.getUnit(), maxKey, keys);
        }

        public Entry(int color, String label, String unit, int maxKey, int... keys) {
            this.color = color;
            this.label = label;
            this.unit = unit;
            this.maxKey = maxKey;
            this.keys = keys;
        }


        public void setLabelText() {
            ylabel.setText(color, label, unit);
        }


        public void setPlotter(int kmFactor, Canvas canvas) {
            plotter =  new GraphPlotter(canvas, getWidth(), getHeight(), 1000 * kmFactor,
                    new AppDensity(getContext()), theme);

        }

        public GraphPlotter getPlotter() {
            return plotter;
        }

        public int getMax(GpxList list) {
            return list.getDelta().getAttributes().getAsInteger(maxKey);
        }

        public void incrementSummaryDistance(float distance) {
            summaryDistance += distance;
        }


        public void plotIfDistance(GpxPointNode point, float minDistance, float distance) {
            if (summaryDistance >= minDistance) {

                final int value = SampleRate.getValue(point.getAttributes(), keys);

                if (value > 0) {
                    //distance += summaryDistance;
                    summaryDistance = 0;

                    plotter.plotData(distance, value, color);
                }
            }

        }
    }

    private static class GraphPainter extends GpxListWalker {
        private final Entry[] entries;

        private float distance=0;
        private final float minDistance;


        public GraphPainter(Entry[] entries, int minDistance) {
            this.entries = entries;
            this.minDistance = minDistance;

        }


        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
        }


        @Override
        public void doPoint(GpxPointNode point) {
            for (Entry e : entries) {
                distance += point.getDistance();

                e.incrementSummaryDistance(point.getDistance());
                e.plotIfDistance(point, minDistance, distance);
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
