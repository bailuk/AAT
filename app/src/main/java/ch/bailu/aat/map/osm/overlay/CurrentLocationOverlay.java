package ch.bailu.aat.map.osm.overlay;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.map.osm.OsmInteractiveView;

public class CurrentLocationOverlay extends OsmOverlay implements OnContentUpdatedInterface{
    private static final int MIN_RADIUS=7;
    private final static int STROKE_WIDTH=2;


    private final GeoPoint center = new GeoPoint(0,0);
    private float accuracy=0f;

    private final Paint paint = new Paint();


    public CurrentLocationOverlay(OsmInteractiveView v, DispatcherInterface d) {
        super(v);

        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(AppTheme.getHighlightColor());
        paint.setAntiAlias(true);

        d.addTarget(this, InfoID.LOCATION);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        center.setLatitudeE6(info.getLatitudeE6());
        center.setLongitudeE6(info.getLongitudeE6());
        accuracy=info.getAccuracy();

        getOsmView().requestRedraw();
    }


    @Override
    public void draw(MapPainter painter) {
        Point pixel = painter.projection.toMapPixels(center);


        if (accuracy > 0) {
            int radius = Math.max(MIN_RADIUS,
                    painter.projection.getPixelFromDistance(accuracy));

            painter.canvas.drawCircle(pixel, radius, paint);
        }
    }

}
