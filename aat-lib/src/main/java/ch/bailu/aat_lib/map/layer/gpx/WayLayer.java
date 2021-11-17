package ch.bailu.aat_lib.map.layer.gpx;

import org.mapsforge.core.graphics.Bitmap;

import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.TwoNodes;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.cache.ObjImageInterface;

public final class WayLayer extends GpxLayer {

    private static final int MAX_VISIBLE_NODES=100;
    private static final int ICON_SIZE=20;

    private final MapContext mcontext;
    private final int icon_size;
    private final ServicesInterface services;

    public WayLayer(MapContext mc,  ServicesInterface services) {
        mcontext = mc;
        this.services = services;
        icon_size = mcontext.getMetrics().getDensity().toPixel_i(ICON_SIZE);
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void drawInside(MapContext mcontext) {
        new WayLayer.WayPainter().walkTrack(getGpxList());
    }

    @Override
    public boolean onTap(ch.bailu.aat_lib.map.Point tapPos) {
        return false;
    }

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}

    private class WayPainter extends GpxListPainter {
        private int count = 0;

        public WayPainter() {
            super(mcontext);
        }


        @Override
        public void drawEdge(TwoNodes nodes) {}


        @Override
        public void drawNode(final TwoNodes.PixelNode node) {
            if (node.isVisible() && count < MAX_VISIBLE_NODES) {

                final Bitmap[] nodeDrawable = {null};

                new InsideContext(services) {
                    @Override
                    public void run() {
                        ObjImageInterface i = services.getIconMapService().getIconSVG(node.point,
                                icon_size);

                        if (i != null)
                            nodeDrawable[0] = i.getBitmap();
                    }
                };

                if (nodeDrawable[0] != null) {
                    mcontext.draw().bitmap(nodeDrawable[0], node.pixel);
                } else {
                    mcontext.draw().bitmap(
                            mcontext.draw().getNodeBitmap(),
                            node.pixel,
                            getColor());
                }

                count++;
            }
        }
    }
}
