package ch.bailu.aat.mapsforge.layer;

import android.content.Context;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppDensity;
import ch.bailu.aat.mapsforge.MapsForgeView;
import ch.bailu.aat.services.ServiceContext;

public class ContextLayer extends MapsForgeLayer {

    private boolean changed = true;
    private final AppDensity density;

    private final MapsForgeView mapView;

    private Point offset = new Point(0,0);
    private Point center = new Point(0,0);
    private Dimension dimension = new Dimension(0,0);


    private Canvas canvas;

    private final Paint textPaint = AndroidGraphicFactory.INSTANCE.createPaint();
    private final int lineHeight;



    public ContextLayer(MapsForgeView map) {
        mapView = map;

        textPaint.setTextSize(70);
        lineHeight = textPaint.getTextHeight("X")+5;

        density = new AppDensity(map.getServiceContext());
    }

    @Override
    public void onSharedPreferenceChanged(String key) {

    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        this.changed = true;

    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        this.canvas = canvas;

        if (changed) {
            changed = false;

            dimension = new Dimension(mapView.getWidth(), mapView.getHeight());
            offset = new Point(
                    (canvas.getWidth() - dimension.width) / 2 ,
                    (canvas.getHeight() - dimension.height) / 2);

            center = new Point(canvas.getWidth()/2, canvas.getHeight()/2);
        }
    }

    public Context getContext() {
        return mapView.getContext();
    }

    public Point getOffset() {
        return offset;
    }

    public Dimension getDimension() {
        return dimension;
    }


    public void drawTextTop(String text, int line) {
        canvas.drawText(text, (int)getOffset().x+5, (int)getOffset().y+lineHeight*line, textPaint);
    }

    public void drawTextBottom(String s, int line) {
        canvas.drawText(s, (int)getOffset().x+5,
                (int)getOffset().y + dimension.height-lineHeight*line, textPaint);
    }

    public MapsForgeView getMapView() {
        return mapView;
    }

    public Point getCenter() {
        return center;
    }

    public ServiceContext getServiceContext() {
        return mapView.getServiceContext();
    }

    public void drawVLine(int x) {
        canvas.drawLine(x, 0, x, canvas.getHeight(), textPaint);
    }

    public void drawHLine(int y) {
        canvas.drawLine(0, y, canvas.getWidth(), y, textPaint);
    }


    public void drawPoint(Point pixel) {
        canvas.drawCircle((int)pixel.x, (int)pixel.y, 10, textPaint);
    }

    public AppDensity getDenisity() {
        return density;
    }
}
