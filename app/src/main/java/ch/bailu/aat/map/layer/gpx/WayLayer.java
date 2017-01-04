package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.util.ui.AppTheme;

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

        icon_size = mcontext.getMetrics().getDensity().toDPi(ICON_SIZE);
    }


    private static int toColor(int c) {
        if (c < 0) return AppTheme.getHighlightColor2();
        return c;
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void draw(MapContext mcontext) {
        new WayLayer.WayPainter().walkTrack(getGpxList());
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


    private class WayPainter extends GpxListPainter {


        private int count=0;

        public WayPainter() {
            super(mcontext);


        }


        @Override
        public void drawEdge(TwoNodes nodes) {

        }




        @Override
        public void drawNode(TwoNodes.PixelNode node) {
            if (node.isVisible() && count < MAX_VISIBLE_NODES) {

                //final Drawable nodeBitmap = mcontext.scontext.getIconMapService().mapIconCache.getIcon(scontext, node.point);

                //if (nodeBitmap != null) {
                //    mcontext.canvas.drawSize(nodeBitmap, node.pixel, icon_size);
                //} else {
                mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, getColor());
                //}
                count++;

            }
        }
    }
}
