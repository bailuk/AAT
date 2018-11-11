package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapPaint;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.views.graph.AltitudeColorTable;

public class RouteLayer extends GpxLayer {

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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void drawInside(MapContext mcontext) {
        if (zoom != mcontext.getMetrics().getZoomLevel() || color != getColor()) {
            zoom = mcontext.getMetrics().getZoomLevel();
            color = getColor();


            paint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity());
            shadow = MapPaint.createEdgePaintBlur(mcontext.draw(),Color.BLACK, zoom);

            paint.setColor(color);
        }


        if (zoom > 9)
            new RouteShadowPainter().walkTrack(getGpxList());

        new RoutePainter().walkTrack(getGpxList());
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

    private class RoutePainter extends GpxListPainter {



        public RoutePainter() {

            super(mcontext,MIN_PIXEL_SPACE);
        }


        @Override
        public void drawEdge(TwoNodes nodes) {
            mcontext.draw().edge(nodes, paint);
        }


        @Override
        public void drawNode(TwoNodes.PixelNode node) {
            int c;
            int altitude=node.point.getAltitude();

            if (altitude == ElevationProvider.NULL_ALTITUDE) c=getColor();
            else c= AltitudeColorTable.INSTANCE.getColor(altitude);

            mcontext.draw().bitmap(mcontext.draw().getNodeBitmap(), node.pixel, c);
        }
    }
}
