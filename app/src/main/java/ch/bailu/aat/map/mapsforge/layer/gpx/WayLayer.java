package ch.bailu.aat.map.mapsforge.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.context.MapContextTwoNodes;

public class WayLayer extends GpxLayer {

    private static final int MAX_VISIBLE_NODES=100;
    private static final int ICON_SIZE=20;

    private final MapContext mcontext;
    private final int icon_size;



    public WayLayer(MapContext mc) {
        this(mc, -1);
    }

    public WayLayer(MapContext mc, int color) {
        super(toColor(color));
        mcontext = mc;

        icon_size = mcontext.metrics.density.toDPi(ICON_SIZE);
    }


    private static int toColor(int c) {
        if (c < 0) return AppTheme.getHighlightColor2();
        return c;
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        new WayLayer.WayPainter().walkTrack(getGpxList());
    }


    private class WayPainter extends GpxListPainter {


        private int count=0;

        public WayPainter() {
            super(mcontext);


        }


        @Override
        public void drawEdge(MapContextTwoNodes nodes) {

        }




        @Override
        public void drawNode(MapContextTwoNodes.PixelNode node) {
            if (node.isVisible() && count < MAX_VISIBLE_NODES) {

                //final Drawable nodeBitmap = mcontext.scontext.getIconMapService().mapIconCache.getIcon(scontext, node.point);

                //if (nodeBitmap != null) {
                //    painter.canvas.drawSize(nodeBitmap, node.pixel, icon_size);
                //} else {
                mcontext.draw.bitmap(mcontext.draw.nodeBitmap.getTileBitmap(), node.pixel, getColor());
                //}
                count++;

            }
        }
    }
}
