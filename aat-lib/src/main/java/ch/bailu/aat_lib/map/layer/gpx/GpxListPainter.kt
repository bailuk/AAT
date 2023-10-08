package ch.bailu.aat_lib.map.layer.gpx;

import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.TwoNodes;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat_lib.map.AppDensity;

public abstract class GpxListPainter extends GpxListWalker {
    private static final int MIN_PIXEL_SPACE=10;
    private static final int MAX_PIXEL_SPACE=100;

    private final static int START_PAINTING=0;
    private final static int CONTINUE_PAINTING =2;


    private final MapContext mcontext;

    private final DistanceCounter edgeDistance, nodeDistance;

    private int action = START_PAINTING;




    public GpxListPainter(MapContext mc, int minPixelSpace) {
        mcontext = mc;
        AppDensity res = mc.getMetrics().getDensity();

        edgeDistance = new DistanceCounter(
                mc.getMetrics().pixelToDistance((int) res.toPixel_f(MIN_PIXEL_SPACE)),
                mc.getMetrics().pixelToDistance((int) res.toPixel_f(MAX_PIXEL_SPACE))
        );

        nodeDistance = new DistanceCounter(
                mc.getMetrics().pixelToDistance((int) res.toPixel_f(minPixelSpace)),
                mc.getMetrics().pixelToDistance((int) res.toPixel_f(MAX_PIXEL_SPACE))
        );

    }
    public GpxListPainter (MapContext mc) {
        this(mc, MIN_PIXEL_SPACE);
    }


    public abstract void drawEdge(TwoNodes nodes);
    public abstract void drawNode(TwoNodes.PixelNode node);


    @Override
    public boolean doList(GpxList track) {
        edgeDistance.reset();
        nodeDistance.reset();
        return mcontext.getMetrics().isVisible(track.getDelta().getBoundingBox());
    }


    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        restartPainting();
        return mcontext.getMetrics().isVisible(segment.getBoundingBox());
    }


    private void restartPainting() {
        action=START_PAINTING;
        edgeDistance.reset();
        nodeDistance.reset();
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        if (mcontext.getMetrics().isVisible(marker.getBoundingBox())) {

            if (action == START_PAINTING) {
                doFirstNode(marker);

            }

            if (marker.getDistance() < edgeDistance.min) {
                doMarkerFirstNode(marker);
                return false;
            }


            return true;

        } else {
            restartPainting();
            return false;
        }
    }






    private void doFirstNode(GpxSegmentNode marker) {
        if (marker.getFirstNode() != null) {
            action = CONTINUE_PAINTING;

            mcontext.getTwoNodes().nodeA.set((GpxPointNode)marker.getFirstNode());
            drawNode(mcontext.getTwoNodes().nodeA);

            edgeDistance.reset();
            nodeDistance.reset();
        }
    }


    private void doMarkerFirstNode(GpxSegmentNode marker) {
        if (marker.getFirstNode() != null) {
            drawNodeIfDistance((GpxPointInterface)marker.getFirstNode());

            edgeDistance.add(marker.getDistance());
            nodeDistance.add(marker.getDistance());
        }
    }


    @Override
    public void doPoint(GpxPointNode point) {
        edgeDistance.add(point.getDistance());
        nodeDistance.add(point.getDistance());
        drawNodeIfDistance(point);
    }


    private void drawNodeIfDistance(GpxPointInterface point) {

        if (edgeDistance.hasDistance() && nodeDistance.isTooSmall()) {
            mcontext.getTwoNodes().nodeB.set(point);
            drawEdgeIfVisible(point);
            edgeDistance.reset();

            mcontext.getTwoNodes().switchNodes();

        } else if (nodeDistance.hasDistance()) {
            mcontext.getTwoNodes().nodeB.set(point);
            drawEdgeIfVisible(point);
            drawNodeIfVisible(point);
            edgeDistance.reset();
            nodeDistance.reset();

            mcontext.getTwoNodes().switchNodes();
        }



    }


    private void drawEdgeIfVisible(GpxPointInterface point) {

        if (    mcontext.getTwoNodes().nodeB.isVisible() ||
                mcontext.getTwoNodes().nodeA.isVisible() ||
                edgeDistance.isTooLarge()) {
            drawEdge(mcontext.getTwoNodes());
        }
    }


    private void drawNodeIfVisible(GpxPointInterface point) {

        if (mcontext.getTwoNodes().nodeB.isVisible()) {
            drawNode(mcontext.getTwoNodes().nodeB);
        }
    }

}
