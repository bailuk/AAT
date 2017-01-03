package ch.bailu.aat.map.mapsforge.layer.gpx;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.context.MapContextTwoNodes;

public abstract class GpxListPainter extends GpxListWalker {
    private static final int MIN_PIXEL_SPACE=10;
    private static final int MAX_PIXEL_SPACE=100;

    private final static int START_PAINTING=0;
    private final static int CONTINUE_PAINTING=2;


    private final MapContext mcontext;

    private final float minDistance, maxDistance;
    private float distanceCount;


    private int action = START_PAINTING;


    public GpxListPainter (MapContext mc) {
        mcontext = mc;
        AppDensity res = mc.metrics.density;

        minDistance = mc.metrics.pixelToDistance((int) res.toDPf(MIN_PIXEL_SPACE));
        maxDistance = mc.metrics.pixelToDistance((int) res.toDPf(MAX_PIXEL_SPACE));

    }


    public abstract void drawEdge(MapContextTwoNodes nodes);
    public abstract void drawNode(MapContextTwoNodes.PixelNode node);


    @Override
    public boolean doList(GpxList track) {
        distanceCount=0;
        return mcontext.metrics.isVisible(track.getDelta().getBoundingBox());
    }


    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        restartPainting();
        return mcontext.metrics.isVisible(segment.getBoundingBox());
    }


    private void restartPainting() {
        action=START_PAINTING;
        distanceCount=0;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        if (mcontext.metrics.isVisible(marker.getBoundingBox())) {

            if (action == START_PAINTING) {
                doFirstNode(marker);

            }

            if (marker.getDistance() < minDistance) {
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

            mcontext.nodes.nodeA.set((GpxPointNode)marker.getFirstNode());
            drawNode(mcontext.nodes.nodeA);

            distanceCount=0;
        }
    }


    private void doMarkerFirstNode(GpxSegmentNode marker) {
        if (marker.getFirstNode() != null) {
            drawNodeIfDistance((GpxPointInterface)marker.getFirstNode());

            distanceCount += marker.getDistance();
        }
    }


    @Override
    public void doPoint(GpxPointNode point) {
        distanceCount += point.getDistance();
        drawNodeIfDistance(point);
    }


    private void drawNodeIfDistance(GpxPointInterface point) {
        if (distanceCount > minDistance) {
            drawNodeIfVisible(point);
            distanceCount=0;
        }
    }


    private void drawNodeIfVisible(GpxPointInterface point) {

        mcontext.nodes.nodeB.set(point);

        if (mcontext.nodes.nodeB.isVisible()) {
            drawEdge(mcontext.nodes);
            drawNode(mcontext.nodes.nodeB);

        } else if (mcontext.nodes.nodeA.isVisible()) {
            drawEdge(mcontext.nodes);

        } else if (distanceCount > maxDistance) {
            drawEdge(mcontext.nodes);
        }

        mcontext.nodes.switchNodes();
    }

}
