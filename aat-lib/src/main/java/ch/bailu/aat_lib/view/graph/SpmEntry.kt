package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.attributes.SampleRate;

public class SpmEntry {
    private final int color;
    private final String label;
    private final String unit;

    private final int maxKey;
    private final int[] keys;

    private GraphPlotter plotter;

    private float summaryDistance=0;


    public SpmEntry(int color, ContentDescription description, int maxKey, int... keys) {
        this(color, description.getLabel(), description.getUnit(), maxKey, keys);
    }

    public SpmEntry(int color, String label, String unit, int maxKey, int... keys) {
        this.color = color;
        this.label = label;
        this.unit = unit;
        this.maxKey = maxKey;
        this.keys = keys;

    }


    public void setLabelText(LabelInterface labels) {
        labels.setText(color, label, unit);
    }


    public void setPlotter(int kmFactor, GraphCanvas canvas, int width, int height) {
        plotter =  new GraphPlotter(canvas, width, height, 1000 * kmFactor);
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
