package ch.bailu.aat.map.layer.control;

import android.util.SparseArray;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.preferences.map.SolidMapGrid;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxNodeFinder;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.Rect;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public abstract class AbsNodeSelectorLayer implements MapLayerInterface, OnContentUpdatedInterface {

    public final static int SQUARE_SIZE = 30;
    public final static int SQUARE_HSIZE = SQUARE_SIZE / 2;

    public final int square_size, square_hsize;

    private final SparseArray<GpxInformation> infoCache =
            new SparseArray<>(5);

    private final Rect centerRect = new Rect();

    private int foundID, foundIndex;
    private GpxPointNode foundNode;

    private final ServicesInterface services;

    private final SolidMapGrid sgrid;
    private MapLayerInterface coordinates;


    public AbsNodeSelectorLayer(ServicesInterface services, StorageInterface s, MapContext mc) {

        this.services = services;
        square_size = mc.getMetrics().getDensity().toPixel_i(SQUARE_SIZE);
        square_hsize = mc.getMetrics().getDensity().toPixel_i(SQUARE_HSIZE);
        centerRect.left = 0;
        centerRect.right = square_size;
        centerRect.top = 0;
        centerRect.bottom = square_size;

        sgrid = new SolidMapGrid(s, mc.getSolidKey());
        coordinates = sgrid.createCenterCoordinatesLayer(services);

    }


    @Override
    public void drawForeground(MapContext mcontext) {
        centerRect.offsetTo(
                mcontext.getMetrics().getWidth()/2 - square_hsize,
                mcontext.getMetrics().getHeight()/2 - square_hsize);

        BoundingBoxE6 centerBounding = new BoundingBoxE6();

        LatLong lt = mcontext.getMetrics().fromPixel(centerRect.left, centerRect.top);
        LatLong rb = mcontext.getMetrics().fromPixel(centerRect.right, centerRect.bottom);

        if (lt != null && rb != null) {
            centerBounding.add(lt);
            centerBounding.add(rb);

            findNodeAndNotify(centerBounding);
        }

        centerRect.offset(mcontext.getMetrics().getLeft(), mcontext.getMetrics().getTop());
        drawSelectedNode(mcontext);
        drawCenterSquare(mcontext);
        coordinates.drawForeground(mcontext);
    }

    @Override
    public void drawInside(MapContext mcontext) {
        coordinates.drawInside(mcontext);
    }



    private void findNodeAndNotify(BoundingBoxE6 centerBounding) {
        if (foundNode == null || centerBounding.contains(foundNode) == false) {

            if (findNode(centerBounding)) {

                setSelectedNode(foundID, infoCache.get(foundID), foundNode, foundIndex);
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


    public abstract void setSelectedNode(int IID, GpxInformation info, GpxPointNode node, int index);

    private void drawSelectedNode(MapContext mcontext) {
        GpxPointNode node = getSelectedNode();

        if (node != null) {
            Point selectedPixel = mcontext.getMetrics().toPixel(node);
            mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), selectedPixel, MapColor.NODE_SELECTED);
        }
    }


    public void drawCenterSquare(MapContext mcontext) {

        mcontext.draw().rect(centerRect, mcontext.draw().getGridPaint());
        mcontext.draw().point(mcontext.getMetrics().getCenterPixel());

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
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (sgrid.hasKey(key)) {
            coordinates = sgrid.createCenterCoordinatesLayer(services);
        }
    }
}
