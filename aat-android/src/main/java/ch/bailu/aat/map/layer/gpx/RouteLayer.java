package ch.bailu.aat.map.layer.gpx;

import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapPaint;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.views.graph.AltitudeColorTable;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.elevation.ElevationProvider;

public final class RouteLayer extends GpxLayer {

    private final int MIN_PIXEL_SPACE = 30;

    private final MapContext mcontext;

    private Paint paint, shadow;
    private int color;
    private int zoom;

    public RouteLayer(MapContext o) {
        mcontext = o;
        color = getColor();
        paint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity());
        shadow = MapPaint.createEdgePaintBlur(mcontext.draw(),color,zoom);

    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void drawInside(MapContext mcontext) {
        if (zoom != mcontext.getMetrics().getZoomLevel() || color != getColor()) {
            zoom = mcontext.getMetrics().getZoomLevel();
            color = getColor();


            paint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity());
            shadow = MapPaint.createEdgePaintBlur(mcontext.draw(),Color.BLACK, zoom);

            paint.setColor(color);
        }


        new RouteShadowPainter().walkTrack(getGpxList());
        new RoutePainterEdge().walkTrack(getGpxList());
        new RoutePainterNode().walkTrack(getGpxList());
    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    private class RouteShadowPainter extends GpxListPainter {
        public RouteShadowPainter() {

            super(mcontext,MIN_PIXEL_SPACE);
        }


        @Override
        public void drawEdge(TwoNodes nodes) {
            mcontext.draw().edge(nodes, shadow);
        }


        @Override
        public void drawNode(TwoNodes.PixelNode node) {

        }
    }

    private class RoutePainterNode extends GpxListPainter {



        public RoutePainterNode() {

            super(mcontext,MIN_PIXEL_SPACE);
        }


        @Override
        public void drawEdge(TwoNodes nodes) {

        }


        @Override
        public void drawNode(TwoNodes.PixelNode node) {
            int c;
            int altitude= (int) node.point.getAltitude();

            if (altitude == ElevationProvider.NULL_ALTITUDE) c=getColor();
            else c= AltitudeColorTable.INSTANCE.getColor(altitude);

            mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, c);
        }
    }


    private class RoutePainterEdge extends GpxListPainter {



        public RoutePainterEdge() {

            super(mcontext,MIN_PIXEL_SPACE);
        }


        @Override
        public void drawEdge(TwoNodes nodes) {
            mcontext.draw().edge(nodes, paint);
        }


        @Override
        public void drawNode(TwoNodes.PixelNode node) {
        }
    }

}
