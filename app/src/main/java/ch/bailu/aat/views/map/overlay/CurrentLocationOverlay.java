package ch.bailu.aat.views.map.overlay;

import org.osmdroid.util.GeoPoint;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.views.map.OsmInteractiveView;

public class CurrentLocationOverlay extends OsmOverlay {
    private static final int MIN_RADIUS=7;
    private final static int STROKE_WIDTH=2;

    
    private final GeoPoint center = new GeoPoint(0,0);
    private float accuracy=0f;

    private final Paint paint = new Paint();


    public CurrentLocationOverlay(OsmInteractiveView v) {
        super(v);

        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(AppTheme.getHighlightColor());
        paint.setAntiAlias(true);
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


    @Override
    public void updateGpxContent(GpxInformation info) {
        if (info.getID()==GpxInformation.ID.INFO_ID_LOCATION) {
            center.setLatitudeE6(info.getLatitudeE6());
            center.setLongitudeE6(info.getLongitudeE6());
            accuracy=info.getAccuracy();
        }
    }
}
