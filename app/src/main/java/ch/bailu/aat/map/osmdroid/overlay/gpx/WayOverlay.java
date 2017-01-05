package ch.bailu.aat.map.osmdroid.overlay.gpx;

import android.graphics.drawable.Drawable;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.map.osmdroid.AbsOsmView;
import ch.bailu.aat.map.osmdroid.overlay.MapPainter;
import ch.bailu.aat.map.osmdroid.overlay.MapTwoNodes;
import ch.bailu.aat.map.osmdroid.overlay.MapTwoNodes.PixelNode;

public class WayOverlay extends GpxOverlay {
    private static final int MAX_VISIBLE_NODES=100;
    private static final int ICON_SIZE=20;

    private final ServiceContext scontext;
    private final int icon_size;



    public WayOverlay(AbsOsmView osm, ServiceContext scontext) {
        this(osm, scontext, -1);
    }

    public WayOverlay(AbsOsmView osm, ServiceContext sc, int color) {
        super(osm, toColor(color));
        scontext = sc;

        icon_size = getOsmView().res.toDPi(ICON_SIZE);
    }


    private static int toColor(int c) {
        if (c < 0) return AppTheme.getHighlightColor2();
        return c;
    }


    @Override
    public void draw(MapPainter p) {
        new WayPainter(p).walkTrack(getGpxList());
    }


    private class WayPainter extends GpxListPainter {

        private final MapPainter painter;
        private int count=0;

        public WayPainter(MapPainter p) {
            super(p, getOsmView().res);
            painter = p;
        }

        
        @Override
        public void drawEdge(MapTwoNodes nodes) {

        }

   


        @Override
        public void drawNode(PixelNode node) {
            if (node.isVisible() && count < MAX_VISIBLE_NODES) {

                final Drawable nodeBitmap = scontext.getIconMapService().getIcon(node.point).getDrawable(scontext.getContext().getResources());

                if (nodeBitmap != null) {
                    painter.canvas.drawSize(nodeBitmap, node.pixel, icon_size);
                } else {
                    painter.canvas.draw(painter.nodeBitmap, node.pixel, getColor());
                }
                count++;

            }
        }
    }
}
