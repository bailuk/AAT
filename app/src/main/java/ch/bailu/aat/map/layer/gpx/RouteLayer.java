package ch.bailu.aat.map.layer.gpx;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.TwoNodes;
import ch.bailu.aat.services.dem.ElevationProvider;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.graph.AltitudeColorTable;

public class RouteLayer extends GpxLayer {

    private final MapContext mcontext;
    private RouteLayer(MapContext o) {
        this(o,  -1);
    }


    public RouteLayer(MapContext o, int color) {
        super(toColor(color));
        mcontext = o;
    }


    private static int toColor(int c) {
        if (c < 0) return AppTheme.getHighlightColor3();
        return c;
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void drawInside(MapContext mcontext) {
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

            super(mcontext);
        }


        @Override
        public void drawEdge(TwoNodes nodes) {
            mcontext.draw().edge(nodes);
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
