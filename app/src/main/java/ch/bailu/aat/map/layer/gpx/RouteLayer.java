package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapPaint;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.views.graph.AltitudeColorTable;

public class RouteLayer extends GpxLayer {

    private final int MIN_PIXEL_SPACE = 30;

    private final MapContext mcontext;

    private Paint edgePaintBlur;
    private Paint edgePaintLine;
    private int color;
    private int zoom;

    public RouteLayer(MapContext o) {
        mcontext = o;
        color = getColor();
        edgePaintBlur = MapPaint.createEdgePaintBlur(mcontext.getMetrics().getDensity(), getColor(), 1);
        edgePaintLine = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity(), 1);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void drawInside(MapContext mcontext) {
        if (zoom != mcontext.getMetrics().getZoomLevel() || color != getColor()) {
            zoom = mcontext.getMetrics().getZoomLevel();
            color = getColor();

            edgePaintBlur = MapPaint.createEdgePaintBlur(mcontext.getMetrics().getDensity(),
                    color, zoom);

            edgePaintLine = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity(), zoom);

            edgePaintLine.setColor(color);
            edgePaintBlur.setColor(MapColor.toDarkTransparent(color));
        }

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


    private class RoutePainter extends GpxListPainter {



        public RoutePainter() {

            super(mcontext,MIN_PIXEL_SPACE);
        }


        @Override
        public void drawEdge(TwoNodes nodes) {
            mcontext.draw().edge(nodes, edgePaintBlur);
            mcontext.draw().edge(nodes, edgePaintLine);
        }


        @Override
        public void drawNode(TwoNodes.PixelNode node) {
            int c;
            int altitude=node.point.getAltitude();

            if (altitude == ElevationProvider.NULL_ALTITUDE) c=getColor();
            else c= AltitudeColorTable.INSTANCE.getColor(altitude);

            mcontext.draw().bitmap(mcontext.draw().getNodeDrawable(), node.pixel, c);
        }
    }
}
