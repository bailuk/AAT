package ch.bailu.aat.map.layer.gpx;

import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.map.Rect;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class GpxTestLayer extends GpxLayer {

    private final MapContext mcontext;

    private final Paint segmentPaint;
    private final Paint markerPaint;
    private int boxCount=0;

    public GpxTestLayer(MapContext mc, DispatcherInterface d, int iid) {
        mcontext = mc;

        segmentPaint = createPaint(Color.BLACK);
        markerPaint = createPaint(Color.DKGRAY);

        d.addTarget(this, iid);
    }

    private Paint createPaint(int color) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
        return paint;
    }



    @Override
    public void drawInside(MapContext mcontext) {
        boxCount = 0;
        new GpxTestLayer.Walker().walkTrack(getGpxList());

        mcontext.draw().textTop(String.valueOf(boxCount),4);
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

    private class Walker extends GpxListWalker {

        public Walker() {

        }


        @Override
        public boolean doList(GpxList track) {
            return true;
        }

        @Override
        public boolean doSegment(GpxSegmentNode segment) {
            return drawBoxIfVisible(segment.getBoundingBox(), segmentPaint);
        }

        @Override
        public boolean doMarker(GpxSegmentNode marker) {
            drawBoxIfVisible(marker.getBoundingBox(), markerPaint);
            return false;
        }

        @Override
        public void doPoint(GpxPointNode point) {}

        private boolean drawBoxIfVisible(BoundingBoxE6 box, Paint paint) {
            if (mcontext.getMetrics().isVisible(box)) {
                boxCount++;
                drawRect(rectFromBox(box), paint);
                return true;
            }
            return false;
        }


        private Rect rectFromBox(BoundingBoxE6 box) {
            return mcontext.getMetrics().toMapPixels(box);
        }


        private void drawRect(Rect rect, Paint paint) {
            mcontext.draw().rect(rect, paint);
        }
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


}
