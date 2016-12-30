package ch.bailu.aat.mapsforge.layer.control;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;

import org.mapsforge.core.graphics.Canvas;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxNodeFinder;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.mapsforge.layer.ContextLayer;
import ch.bailu.aat.mapsforge.layer.MapsForgeLayer;

public abstract class NodeSelectorLayer extends MapsForgeLayer {

    private final static int COLOR = 0xccffffff;


    public final int SQUARE_SIZE = 30;
    public final int SQUARE_HSIZE = SQUARE_SIZE / 2;

    public final int square_size, square_hsize;

    private final SparseArray<GpxInformation> gpxHash =
            new SparseArray<>(5);

    private final Rect centerRect = new Rect();
    private final Point selectedPixel = new Point();

    private int foundID, foundIndex;
    private GpxPointNode foundNode;

    private final ContextLayer clayer;

    public NodeSelectorLayer(ContextLayer cl) {

        clayer = cl;

        square_size = cl.getDenisity().toDPi(SQUARE_SIZE);
        square_hsize = cl.getDenisity().toDPi(SQUARE_HSIZE);
        centerRect.left = 0;
        centerRect.right = square_size;
        centerRect.top = 0;
        centerRect.bottom = square_size;

    }


    @Override
    public void draw(org.mapsforge.core.model.BoundingBox boundingBox, byte zoomLevel, Canvas canvas, org.mapsforge.core.model.Point topLeftPoint) {
     /*
        boundingBox.maxLatitude/2
        centerRect.offsetTo(w().getWidth() / 2 - square_hsize,
                getOsmView().getHeight() / 2 - square_hsize);

        BoundingBox centerBounding = new BoundingBox();
        centerBounding.add(p.projection.fromPixels(centerRect.left, centerRect.top));
        centerBounding.add(p.projection.fromPixels(centerRect.right, centerRect.bottom));


        centerRect.offset(p.projection.screen.left, p.projection.screen.top);

        findNodeAndNotify(centerBounding);

        drawSelectedNode(p);
        drawCenterSquare(p);
*/
    }



    private void findNodeAndNotify(BoundingBox centerBounding) {
        if (foundNode == null || centerBounding.contains(foundNode) == false) {

            if (findNode(centerBounding)) {
                setSelectedNode(gpxHash.get(foundID), foundNode, foundIndex);
            }
        }
    }


    private boolean findNode(BoundingBox centerBounding) {
        boolean found = false;

        for (int i = 0; i < gpxHash.size() && found == false; i++) {
            GpxList list = gpxHash.valueAt(i).getGpxList();
            GpxNodeFinder finder = new GpxNodeFinder(centerBounding);

            finder.walkTrack(list);
            if (finder.haveNode()) {
                found = true;
                foundID = gpxHash.keyAt(i);
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
/*
        if (node != null) {
            painter.projection.toPixels(node, selectedPixel);
            clayer.draw(clayer.nodeBitmap, selectedPixel, COLOR);
        }*/
    }


    public void drawCenterSquare() {
        /*
        clayer.drawRect(centerRect);
        clayer.drawPoint(clayer.getCenter());
        */
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (info.isLoaded()) {
            gpxHash.put(iid, info);

        } else {
            gpxHash.remove(iid);
        }
    }

    @Override
    public void onSharedPreferenceChanged(String key) {

    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
