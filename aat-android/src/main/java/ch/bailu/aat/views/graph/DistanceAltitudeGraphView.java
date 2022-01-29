package ch.bailu.aat.views.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.util.color.AltitudeColorTable;
import ch.bailu.aat_lib.view.graph.GraphCanvas;
import ch.bailu.aat_lib.view.graph.GraphPlotter;
import ch.bailu.aat_lib.view.graph.Segment;

public class DistanceAltitudeGraphView extends AbsGraphView {



    public DistanceAltitudeGraphView(Context context, DispatcherInterface di,
                                     UiTheme theme, int... iid) {
        super(context, di, theme, iid);
        ylabel.setText(Color.WHITE, R.string.altitude, sunit.getAltitudeUnit());
       }


    public DistanceAltitudeGraphView(Context context, UiTheme theme) {
        super(context, theme);
        ylabel.setText(Color.WHITE, R.string.altitude, sunit.getAltitudeUnit());
    }


    @Override
    public void plot(Canvas canvas, GpxList list, Segment segment, int index, SolidUnit sunit,
                     boolean markerMode) {

        DistanceWalker distances = new DistanceWalker(segment);
        distances.walkTrack(list);

        int km_factor = (int) (distances.getDistanceDelta()/1000) + 1;
        GraphCanvas graphCanvas = new AndroidCanvas(canvas, new AndroidAppDensity(getContext()), theme);
        GraphPlotter plotter = new GraphPlotter(graphCanvas, getWidth(), getHeight(), 1000 * km_factor);

        GpxListWalker painter, scaleGenerator;

        int minDistance = (int)distances.getDistanceDelta() / getWidth();
        if (segment.hasLimit()) {
            painter = new GraphPainterLimit(plotter, segment, minDistance);
            scaleGenerator = new ScaleGeneratorLimit(plotter, segment);

        } else if (markerMode) {
            painter = new GraphPainterMarkerMode(plotter, minDistance);
            scaleGenerator = new ScaleGeneratorMarkerMode(plotter);
        } else {
            painter = new GraphPainter(plotter, minDistance);
            scaleGenerator = new ScaleGenerator(plotter);

        }

        scaleGenerator.walkTrack(list);
        plotter.roundYScale(50);


        painter.walkTrack(list);


        new SegmentNodePainter(plotter, distances.getDistanceOffset()).walkTrack(list);
        if (index > -1) {
            new IndexPainter(plotter, index, distances.getDistanceOffset()).walkTrack(list);
        }

        plotter.drawXScale(5, sunit.getDistanceFactor(), isXLabelVisible());
        plotter.drawYScale(5, sunit.getAltitudeFactor(), true);
    }


    private static class DistanceWalker extends GpxListWalker {
        private int index = 0;
        private float dstDelta = 0f;
        private float dstOffset = 0f;
        private final Segment segment;

        public DistanceWalker(Segment segment) {
            this.segment = segment;
        }

        @Override
        public boolean doList(GpxList track) {
            if (segment.hasLimit()) {
                return true;
            } else {
                dstDelta = track.getDelta().getDistance();
                return false;
            }
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return doDelta(segment.getSegmentSize(), segment.getDistance());
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return doDelta(marker.getSegmentSize(), marker.getDistance());
        }


        private boolean doDelta(int size, float distance) {

            if (segment.isAfter(index)) {
                return false;

            } else {
                int nextIndex = index + size;

                if (segment.isBefore(nextIndex)) {
                    index = nextIndex;
                    dstOffset += distance;
                    return false;
                }
            }
            return true;
        }


        @Override
        public void doPoint(GpxPointNode point) {
            if (segment.isBefore(index)) {
                dstOffset += point.getDistance();

            } else if (segment.isNotAfter(index)) {
                dstDelta += point.getDistance();
            }
            index++;
        }


        public float getDistanceDelta() {
            return dstDelta;
        }

        public float getDistanceOffset() {
            return dstOffset;
        }
    }


    private static class ScaleGenerator extends GpxListWalker {
        private final GraphPlotter plotter;


        public ScaleGenerator(GraphPlotter p) {
            plotter=p;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            plotter.inlcudeInYScale((float)point.getAltitude());
        }


        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return true;
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


    private static class ScaleGeneratorLimit extends ScaleGenerator {
        private int index = 0;
        private final Segment segment;

        public ScaleGeneratorLimit(GraphPlotter p, Segment segment) {
            super(p);
            this.segment = segment;
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return doDelta(marker.getSegmentSize());
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return doDelta(segment.getSegmentSize());
        }

        private boolean doDelta(int size) {

            if (segment.isAfter(index)) {
                return false;

            } else {
                int nextIndex = index + size;

                if (segment.isBefore(nextIndex)) {
                    index = nextIndex;
                    return false;
                }
            }
            return true;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            if (segment.isInside(index)) {
                super.doPoint(point);
            }
            index++;
        }
    }


    private static class ScaleGeneratorMarkerMode extends ScaleGenerator {

        public ScaleGeneratorMarkerMode(GraphPlotter p) {
            super(p);
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            GpxPointNode point = (GpxPointNode) marker.getFirstNode();
            if (point != null) {
                doPoint(point);
            }
            return false;
        }
    }



    private static class GraphPainter extends GpxListWalker {
        private final GraphPlotter plotter;

        private float distance=0;
        private float summaryDistance=0;
        private final float minDistance;


        public GraphPainter(GraphPlotter p, int md) {
            plotter=p;
            minDistance=md*SAMPLE_WIDTH_PIXEL;
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
                int altitude = (int)point.getAltitude();

                distance+=summaryDistance;
                summaryDistance=0;

                plotter.plotData(distance, altitude, AltitudeColorTable.instance().getColor(altitude));
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


    private static class GraphPainterLimit extends GraphPainter {
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


    private static class GraphPainterMarkerMode extends GraphPainter {

        public GraphPainterMarkerMode(GraphPlotter p, int md) {
            super(p, md);
        }


        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            if (marker.getFirstNode() != null) {
                plotIfDistance((GpxPointNode)marker.getFirstNode());
                incrementSummaryDistance(marker.getDistance());
            }
            return false;
        }
    }


    private static class IndexPainter extends GpxListWalker {

        private float distance = 0f;
        private final float offset;
        private int index = 0;

        private final int nodeIndex;
        private final GraphPlotter plotter;

        public IndexPainter(GraphPlotter p, int n, float offset) {
            nodeIndex = n;
            plotter = p;
            this.offset = offset;
        }

        @Override
        public boolean doList(GpxList track) {
            return true;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return doDelta(segment);
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return doDelta(marker);
        }


        private boolean doDelta(GpxSegmentNode segment) {
            if (index + segment.getSegmentSize() <= nodeIndex) {
                index += segment.getSegmentSize();
                distance += segment.getDistance();
                return false;
            }
            return true;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            if (index == nodeIndex) {
                distance += point.getDistance();
                plotPoint(point, distance - offset);
                index++;

            } else if (index < nodeIndex) {
                distance += point.getDistance();
                index++;
            }
        }

        private void plotPoint(GpxPointNode point, float distance) {
            plotter.plotPoint(distance, (float) point.getAltitude(),
                    MapColor.NODE_SELECTED);
        }
    }


    private static class SegmentNodePainter extends GpxListWalker {

        private float distance;

        private final GraphPlotter plotter;

        public SegmentNodePainter(GraphPlotter p, float offset) {
            plotter = p;
            distance = 0f - offset;

        }

        @Override
        public boolean doList(GpxList track) {
            return true;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            if (segment.getSegmentSize() > 0 && distance > 0f) {
                GpxPointNode node = (GpxPointNode) segment.getFirstNode();
                plotPoint(node, distance + node.getDistance());
            }

            distance += segment.getDistance();
            return false;

        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return false;
        }


        @Override
        public void doPoint(GpxPointNode point) {}

        private void plotPoint(GpxPointNode point, float distance) {
            plotter.plotPoint(distance, (float) point.getAltitude(),
                    MapColor.NODE_NEUTRAL);
        }
    }
}
