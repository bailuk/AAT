package ch.bailu.aat_lib.view.graph;

public class Segment {
    private int firstPoint = -1;
    private int lastPoint = -1;

    public void setLimit(int first, int last) {
        firstPoint = first;
        lastPoint = last;
    }

    public boolean hasLimit() {
        return firstPoint > -1 && lastPoint > firstPoint;
    }

    public boolean isAfter(int index) {
        return index > lastPoint;
    }

    public boolean isBefore(int index) {
        return index < firstPoint;
    }

    public boolean isNotAfter(int index) {
        return index <= lastPoint;
    }

    public boolean isInside(int index) {
        return index >= firstPoint && index <= lastPoint;
    }
}
