package ch.bailu.aat.map;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.map.MapDraw;

public final class MapPaint {


    public static final int EDGE_WIDTH_LINE=2;

    private final static float TEXT_SIZE=20;


    public static Paint createBackgroundPaint(int color) {
        Paint p = AndroidGraphicFactory.INSTANCE.createPaint();
        //p.setColor(color);


        p.setColor(MapColor.toLightTransparent(color));
        p.setStyle(Style.FILL);
        return p;
    }

    public static Paint createBackgroundPaint() {
        Paint p= AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(MapColor.LIGHT);
        p.setStyle(Style.FILL);
        return p;
    }

    public static Paint  createGridPaint(AppDensity res) {
        Paint p=AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(MapColor.GRID);
        p.setStyle(Style.FILL);
        p.setStrokeWidth(Math.max(1, res.toPixel_f(1)));
        return p;
    }


    public static Paint createStatusTextPaint(AppDensity res) {
        return createTextPaint(res, TEXT_SIZE);
    }

    public static Paint createLegendTextPaint(AppDensity res) {
        Paint p =  createTextPaint(res, TEXT_SIZE/3*2);
        p.setColor(MapColor.TEXT);

        return p;
    }


    public static Paint  createTextPaint(AppDensity res, float size) {
        Paint p=AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(MapColor.TEXT);

        p.setTextSize(res.toPixelScaled_f(size));
        AndroidGraphicFactory.getPaint(p).setFakeBoldText(true);
        p.setStyle(Style.FILL);

        return p;
    }


    public static Paint createEdgePaintLine(AppDensity res) {
        Paint edge = AndroidGraphicFactory.INSTANCE.createPaint();

        edge.setStrokeWidth(Math.max(res.toPixel_f(EDGE_WIDTH_LINE), 1));
        edge.setColor(MapColor.EDGE);
        edge.setStyle(Style.STROKE);

        return edge;
    }


    public static Paint createEdgePaintBlur(MapDraw draw, int color, int zoom) {
        Paint edge = AndroidGraphicFactory.INSTANCE.createPaint();

        color = MapColor.setAlpha(color, zoomToAlpha(zoom));

        edge.setStrokeWidth(draw.getNodeBitmap().getWidth());
        edge.setColor(color);
        edge.setStyle(Style.STROKE);



        return edge;
    }


    private static int zoomToAlpha(int z) {
        int a = 20;

        if (z > 15) a = 100;
        else if (z > 13) a = 90;
        else if (z > 12) a = 80;
        else if (z > 11) a = 70;
        else if (z > 10) a = 60;
        else if (z > 9) a = 50;
        else if (z > 8) a = 40;
        else if (z > 7) a = 30;





        return a;
    }
}
