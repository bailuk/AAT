package ch.bailu.aat.map;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;

public class MapPaint {


    public static final int EDGE_WIDTH=2;
    private final static float TEXT_SIZE=20;

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
        p.setColor(AppTheme.getAltBackgroundColor());
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


    public static Paint createEdgePaint(AppDensity res) {
        Paint edge = AndroidGraphicFactory.INSTANCE.createPaint();

        edge.setStrokeWidth(res.toPixel_f(EDGE_WIDTH));
        edge.setColor(MapColor.EDGE);
        edge.setStyle(Style.STROKE);


        return edge;
    }

}
