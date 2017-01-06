package ch.bailu.aat.map;

import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppTheme;

public class MapPaint {


    public static final int EDGE_WIDTH=2;
    private final static float TEXT_SIZE=20;
    private final static int ALPHA=150;
    private final static int BG_ALPHA=150;

    public static Paint createBackgroundPaint() {
        Paint p= AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(Color.WHITE);

        AndroidGraphicFactory.getPaint(p).setAlpha(BG_ALPHA);
        p.setStyle(Style.FILL);
        return p;
    }

    public static Paint  createGridPaint(AppDensity res) {
        Paint p=AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(Color.GRAY);
        p.setStyle(Style.FILL);
        p.setStrokeWidth(Math.max(1, res.toDPf(1)));
        return p;
    }


    public static Paint createStatusTextPaint(AppDensity res) {
        Paint p = createTextPaint(res, TEXT_SIZE);
        return p;
    }

    public static Paint createLegendTextPaint(AppDensity res) {
        Paint p =  createTextPaint(res, TEXT_SIZE/3*2);
        p.setColor(AppTheme.getAltBackgroundColor());
        return p;
    }


    public static Paint  createTextPaint(AppDensity res, float size) {
        Paint p=AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(Color.BLACK);

        p.setTextSize(res.toSDPf(size));
        AndroidGraphicFactory.getPaint(p).setFakeBoldText(true);
        p.setStyle(Style.FILL);

        return p;
    }


    public static Paint createEdgePaint(AppDensity res) {
        Paint edge = AndroidGraphicFactory.INSTANCE.createPaint();

        edge.setStrokeWidth(res.toDPf(EDGE_WIDTH));
        edge.setColor(AppTheme.getAltBackgroundColor());
        edge.setStyle(Style.STROKE);


        return edge;
    }

}
