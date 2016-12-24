package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxNodeFinder;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public abstract class NodeSelectorOverlay extends OsmOverlay implements OnContentUpdatedInterface {
    private final static int COLOR = 0xccffffff;


    public final int SQUARE_SIZE=30;
    public final int SQUARE_HSIZE=SQUARE_SIZE/2;

    public final int square_size, square_hsize;

    private final SparseArray<GpxInformation> gpxHash =
            new SparseArray<>(5);

    private final Rect centerRect = new Rect();
    private final Point selectedPixel = new Point();

    private int  foundID, foundIndex;
    private GpxPointNode foundNode;


    public NodeSelectorOverlay(OsmInteractiveView v) {
        super(v);
        square_size = getOsmView().res.toDPi(SQUARE_SIZE);
        square_hsize = getOsmView().res.toDPi(SQUARE_HSIZE);
        centerRect.left=0;
        centerRect.right=square_size;
        centerRect.top=0;
        centerRect.bottom=square_size;

    }


    @Override
    public void draw(MapPainter p) {

        centerRect.offsetTo(getOsmView().getWidth()/2  - square_hsize,
                getOsmView().getHeight()/2 - square_hsize);

        BoundingBox centerBounding = new BoundingBox();
        centerBounding.add(p.projection.fromPixels(centerRect.left, centerRect.top));
        centerBounding.add(p.projection.fromPixels(centerRect.right, centerRect.bottom));


        centerRect.offset(p.projection.screen.left, p.projection.screen.top);

        findNodeAndNotify(centerBounding);

        drawSelectedNode(p);
        drawCenterSquare(p);
    }


    private void findNodeAndNotify(BoundingBox centerBounding) {
        if (foundNode == null || centerBounding.contains(foundNode) == false) {

            if (findNode(centerBounding)) {
                setSelectedNode(gpxHash.get(foundID), foundNode, foundIndex);
            }
        }
    }





    private boolean findNode(BoundingBox centerBounding) {
        boolean found=false;

        for (int i=0; i < gpxHash.size() && found==false; i++) {
            GpxList list = gpxHash.valueAt(i).getGpxList();
            GpxNodeFinder finder=new GpxNodeFinder(centerBounding);

            finder.walkTrack(list);
            if (finder.haveNode()) {
                found=true;
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

    private void drawSelectedNode(MapPainter painter) {
        GpxPointNode node = getSelectedNode();

        if (node != null) {
            painter.projection.toPixels(node, selectedPixel);
            painter.canvas.draw(painter.nodeBitmap, selectedPixel, COLOR);
        }
    }


    public void drawCenterSquare(MapPainter p) {
        p.canvas.drawRect(centerRect);

        Point point = new Point(centerRect.centerX(), centerRect.centerY());
        p.canvas.drawPoint(point);
    }



    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (info.isLoaded()) {
            gpxHash.put(iid, info);

        } else {
            gpxHash.remove(iid);
        }
    }
}
