package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Point;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.MapTwoNodes;
import ch.bailu.aat.views.map.overlay.MapTwoNodes.PixelNode;

public abstract class GpxLegendOverlay extends GpxOverlay {

    private static int MIN_PIXEL_DISTANCE=100;

    private MapPainter painter;
    private final boolean resetAfterDraw;

    private MapTwoNodes nodes;

    private static int COLOR = 0x99ffffff;

    public GpxLegendOverlay(AbsOsmView osmPreview, int id) {
        this(osmPreview,id,false);
    }


    public GpxLegendOverlay(AbsOsmView osmPreview, int id, boolean reset) {
        super(osmPreview, id, COLOR);

        resetAfterDraw=reset;
    }


    @Override
    public void draw(MapPainter p) {
        painter=p;
        nodes = painter.nodes;
    }

    
    private abstract class AbsWalker extends GpxListWalker {
        @Override
        public boolean doList(GpxList track) {
            if (track.getPointList().size() > 0 && painter.projection.isVisible(track.getDelta().getBoundingBox())) {
                painter.canvas.draw(painter.nodeBitmap, nodes.nodeA.pixel, COLOR);
                return true;
            }
            return false;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return true;
        }
    }



    public class PointDistanceWalker extends AbsWalker {
        private float distance=0;

        @Override
        public boolean doList(GpxList l) {
            distance=0;
            return super.doList(l);
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            if (painter.projection.isVisible(marker.getBoundingBox())) {
                distance -= ((GpxPointNode)marker.getFirstNode()).getDistance();
                return true;
            } else {
                distance+=marker.getDistance();
                return false;
            }
        }

        @Override
        public void doPoint(GpxPointNode point) {
            nodes.nodeB.set(point);

            distance += point.getDistance();

            if (!nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                drawLegendIfVisible(nodes.nodeB, distance);
                
                nodes.switchNodes();
                
                if (resetAfterDraw) 
                    distance=0;
            }
        }
    };


    public class SegmentIndexWalker extends AbsWalker {
        private int index=1;

        @Override
        public boolean doList(GpxList l) {
            index=1;
            return super.doList(l);
        }


        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            nodes.nodeB.set((GpxPointNode)segment.getFirstNode());

            if (!nodes.arePointsTooClose(MIN_PIXEL_DISTANCE) || index == 1) {
                drawLegendNodeIfVisible(nodes.nodeB, index);
                
                nodes.switchNodes();
            }
            index++;
            return segment.getNext()==null;
        }


        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return marker.getNext()==null;
        }

        @Override
        public void doPoint(GpxPointNode point) {
            if (point.getNext()==null) {
                nodes.nodeB.set((GpxPointNode)point);

                painter.canvas.draw(painter.nodeBitmap, nodes.nodeB.pixel, getColor());
                if (!nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                    drawLegend(painter, nodes.nodeB.pixel, index);
                }
            }
        }
    }


    public class PointIndexWalker extends AbsWalker {

        private int index=1;

        @Override
        public boolean doList(GpxList l) {
            index=1;
            return super.doList(l);
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            return painter.projection.isVisible(marker.getBoundingBox());
        }

        @Override
        public void doPoint(GpxPointNode point) {
            nodes.nodeB.set(point);

            if (!nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                drawLegendIfVisible(nodes.nodeB, index);
                nodes.switchNodes();

            }
            index++;
        }
    };


    public class MarkerDistanceWalker extends AbsWalker {

        private float distance=0;

        @Override
        public boolean doList(GpxList l) {
            if (super.doList(l)) {
                distance=0;
                nodes.nodeA.set((GpxPointNode)l.getPointList().getFirst());
                drawNodeIfVisible(nodes.nodeA);
                
                return true;
            }
            return false;
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            boolean isLast = marker.getNext() == null;

            if (!isLast) {
                nodes.nodeB.set((GpxPointNode)marker.getFirstNode());

                if (!nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                    drawLegendNodeIfVisible(nodes.nodeB, distance);
                    
                    nodes.switchNodes();
                    if (resetAfterDraw) distance=0;
                }

            }
            distance += marker.getDistance();
            return isLast;
        }


        @Override
        public void doPoint(GpxPointNode point) {
            if (point.getNext()==null) {
                nodes.nodeB.set(point);

                if (nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                    drawNodeIfVisible(nodes.nodeB);
                    
                } else {
                    drawLegendNodeIfVisible(nodes.nodeB, distance);
                }
            }
        }

    }




    public class MarkerAltitudeWalker extends AbsWalker {

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            boolean isLast = marker.getNext() == null;

            if (!isLast) {
                nodes.nodeB.set((GpxPointNode)marker.getFirstNode());

                if (!nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                    drawLegendNodeIfVisible(nodes.nodeB, ((GpxPointNode)marker.getFirstNode()).getAltitude());
                
                    nodes.switchNodes();
                }

            }

            return isLast;
        }




        @Override
        public void doPoint(GpxPointNode point) {
            if (point.getNext()==null) {
                nodes.nodeB.set(point);

                if (nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                    painter.canvas.draw(painter.nodeBitmap, nodes.nodeB.pixel, getColor());
                } else {
                    painter.canvas.draw(painter.nodeBitmap, nodes.nodeB.pixel, getColor());
                    drawLegend(painter, nodes.nodeB.pixel, point.getAltitude());
                }
            }
        }
    }


    public class MarkerSpeedWalker extends AbsWalker {

        float speed;
        int samples;

        @Override
        public boolean doList(GpxList l) {
            speed=0;
            samples=0;
            return super.doList(l);
        }


        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            boolean isLast = marker.getNext() == null;

            if (!isLast) {
                nodes.nodeB.set((GpxPointNode)marker.getFirstNode());

                if (!nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                    if (samples >0) speed = speed / samples;

                    drawLegendNodeIfVisible(nodes.nodeB, speed);
                    nodes.switchNodes();

                    speed=0;
                    samples=0;

                }

            }

            speed = speed + marker.getSpeed();
            samples++;

            return isLast;
        }


        @Override
        public void doPoint(GpxPointNode point) {
            if (point.getNext()==null) {
                nodes.nodeB.set(point);

                if (nodes.arePointsTooClose(MIN_PIXEL_DISTANCE)) {
                    drawNodeIfVisible(nodes.nodeB);
                } else {
                    speed = speed / samples;
                    drawLegendNodeIfVisible(nodes.nodeB, speed);
                }
            }
        }
    }

    public void drawText(Point pixel, String text) {
        painter.canvas.drawText(text, pixel);
    }


    private void drawNodeIfVisible(PixelNode node) {
        if (painter.projection.isVisible(node.point))
            painter.canvas.draw(painter.nodeBitmap, node.pixel, getColor());
    }

    
    private void drawLegendIfVisible(PixelNode node, float f) {
        if (painter.projection.isVisible(node.point))
            drawLegend(painter, node.pixel, f);
    }
    
    private void drawLegendIfVisible(PixelNode node, int i) {
        if (painter.projection.isVisible(node.point))
            drawLegend(painter, node.pixel, i);
    }


    private void drawLegendNodeIfVisible(PixelNode node, float f) {
        if (painter.projection.isVisible(node.point)) {
            painter.canvas.draw(painter.nodeBitmap, node.pixel, getColor());
            drawLegend(painter, node.pixel,f); 
        }
    }
    
    private void drawLegendNodeIfVisible(PixelNode node, int i) {
        if (painter.projection.isVisible(node.point)) {
            painter.canvas.draw(painter.nodeBitmap, node.pixel, getColor());
            drawLegend(painter, node.pixel,i); 
        }
    }
    
    public abstract void drawLegend(MapPainter painter, Point pixel, int value);
    public abstract void drawLegend(MapPainter painter, Point pixel, float value);
}
