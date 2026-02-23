package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxPointNode;

public class GraphPainterLimit extends GraphPainter {
    private int index = 0;
    private final Segment segment;

    public GraphPainterLimit(GraphPlotter p, Segment segment, int md) {
        super(p, md);
        this.segment = segment;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (segment.isInside(index)) {
            super.doPoint(point);
        }
        index++;
    }
}