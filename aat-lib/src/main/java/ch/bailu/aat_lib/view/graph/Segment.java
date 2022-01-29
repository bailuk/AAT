package ch.bailu.aat_lib.view.graph;


public class Segment {
    private int start = -1;
    private int end = -1;

    public void setLimit(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean isValid() {
        return start > -1 && end > start;
    }

    public boolean isAfter(int index) {
        return index > end;
    }

    public boolean isBefore(int index) {
        return index < start;
    }

    public boolean isInside(int index) {
        return index >= start && index <= end;
    }
}
