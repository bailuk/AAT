package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxList;

public interface PlotterConfig {
    int getWidth();
    int getHeight();

    GpxList getList();
    int getIndex();

    boolean isXLabelVisible();

    LabelInterface getLabels();
}
