package ch.bailu.aat_lib.view.graph;

public abstract class Plotter {
    public abstract void plot(GraphCanvas canvas, PlotterConfig config);
    public abstract void initLabels(LabelInterface labels);
    public abstract void setLimit(int firstPoint, int lastPoint);
}
