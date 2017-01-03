package ch.bailu.aat.map.mapsforge.layer.control;

import android.graphics.Rect;
import android.util.SparseArray;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxNodeFinder;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.util.graphic.Pixel;

public abstract class NodeSelectorLayer extends MapsForgeLayer implements OnContentUpdatedInterface {

    private final static int COLOR = 0xccffffff;


    public final int SQUARE_SIZE = 30;
    public final int SQUARE_HSIZE = SQUARE_SIZE / 2;

    public final int square_size, square_hsize;

    private final SparseArray<GpxInformation> infoCache =
            new SparseArray<>(5);

    private final Rect centerRect = new Rect();
    private Pixel selectedPixel = new Pixel();

    private int foundID, foundIndex;
    private GpxPointNode foundNode;

    private final MapContext mcontext;

    public NodeSelectorLayer(MapContext cl) {

        mcontext = cl;

        square_size = cl.metrics.density.toDPi(SQUARE_SIZE);
        square_hsize = cl.metrics.density.toDPi(SQUARE_HSIZE);
        centerRect.left = 0;
        centerRect.right = square_size;
        centerRect.top = 0;
        centerRect.bottom = square_size;

    }


    @Override
    public void draw(org.mapsforge.core.model.BoundingBox boundingBox, byte zoomLevel, Canvas canvas, org.mapsforge.core.model.Point topLeftPoint) {

        //boundingBox.maxLatitude/2;

        //Pixel center = mcontext.metrics.getCenterPixel();
        centerRect.offsetTo(
                mcontext.metrics.getWidth()/2 - square_hsize,
                mcontext.metrics.getHeight()/2 - square_hsize);

        BoundingBoxE6 centerBounding = new BoundingBoxE6();

        LatLong lt = mcontext.metrics.fromPixel(centerRect.left, centerRect.top);
        LatLong rb = mcontext.metrics.fromPixel(centerRect.right, centerRect.bottom);

        if (lt != null && rb != null) {
            centerBounding.add(lt);
            centerBounding.add(rb);

            findNodeAndNotify(centerBounding);
        }

        centerRect.offset(mcontext.metrics.getLeft(), mcontext.metrics.getTop());
        drawSelectedNode();
        drawCenterSquare();

    }



    private void findNodeAndNotify(BoundingBoxE6 centerBounding) {
        if (foundNode == null || centerBounding.contains(foundNode) == false) {

            if (findNode(centerBounding)) {

                setSelectedNode(infoCache.get(foundID), foundNode, foundIndex);
            }
        }
    }


    private boolean findNode(BoundingBoxE6 centerBounding) {
        boolean found = false;

        for (int i = 0; i < infoCache.size() && found == false; i++) {
            GpxList list = infoCache.valueAt(i).getGpxList();
            GpxNodeFinder finder = new GpxNodeFinder(centerBounding);

            finder.walkTrack(list);
            if (finder.haveNode()) {
                found = true;
                foundID = infoCache.keyAt(i);
                foundIndex = finder.getNodeIndex();
                foundNode = finder.getNode();
            }
        }
        return found;
    }


    public GpxPointNode getSelectedNode() {
        return foundNode;
    }


    public abstract void setSelectedNode(GpxInformation info, GpxPointNode node, int index);

    private void drawSelectedNode() {
        GpxPointNode node = getSelectedNode();

        if (node != null) {
            selectedPixel = mcontext.metrics.toPixel(node);
            mcontext.draw.bitmap(mcontext.draw.nodeBitmap.getTileBitmap(), selectedPixel, COLOR);
        }
    }


    public void drawCenterSquare() {

        mcontext.draw.rect(centerRect, mcontext.draw.gridPaint);
        mcontext.draw.point(mcontext.metrics.getCenterPixel());

    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (info.isLoaded()) {
            infoCache.put(iid, info);

        } else {
            infoCache.remove(iid);
        }
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
