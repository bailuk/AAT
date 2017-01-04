package ch.bailu.aat.map;

import android.content.Context;
import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppDensity;

public class MapPaint {


    public static final int EDGE_WIDTH=1;
    private final static float TEXT_SIZE=20;
    private final static int ALPHA=150;
    private final static int BG_ALPHA=125;

    public static Paint createBackgroundPaint() {
        org.mapsforge.core.graphics.Paint p= AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(Color.WHITE);
        //p.setAlpha(BG_ALPHA);
        p.setStyle(Style.FILL);
        //p.setAntiAlias(false);
        return p;
    }

    public static Paint  createGridPaint(AppDensity res) {
        org.mapsforge.core.graphics.Paint p=AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(Color.GREEN);
        //p..setAlpha(ALPHA);
        p.setStyle(Style.STROKE);
        //p.setAntiAlias(false);
        //p.setDither(false);
        p.setStrokeWidth(Math.max(1, res.toDPf(1)));
        return p;
    }


    public static Paint createStatusTextPaint(AppDensity res) {
        return createTextPaint(res, TEXT_SIZE);
    }

    public static Paint createLegendTextPaint(AppDensity res) {
        return createTextPaint(res, TEXT_SIZE/3*2);
    }


    public static Paint  createTextPaint(AppDensity res, float size) {
        org.mapsforge.core.graphics.Paint p=AndroidGraphicFactory.INSTANCE.createPaint();
        p.setColor(Color.BLUE);

        p.setTextSize(res.toSDPf(size));
        //p.setFakeBoldText(true);
        p.setStyle(Style.FILL);
        //p.setAntiAlias(true);

        return p;
    }


    public static Paint createEdgePaint(AppDensity res) {
        org.mapsforge.core.graphics.Paint edge = AndroidGraphicFactory.INSTANCE.createPaint();

        edge.setStrokeWidth(res.toDPf(EDGE_WIDTH));

        //edge.setAntiAlias(false);
        edge.setColor(Color.CYAN);
        edge.setStyle(Style.STROKE);
        return edge;
    }

}
