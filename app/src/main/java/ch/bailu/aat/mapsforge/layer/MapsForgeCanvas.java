package ch.bailu.aat.mapsforge.layer;

import android.content.Context;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.helpers.AppDensity;

public class MapsForgeCanvas {
    public static final int EDGE_WIDTH=1;

    private final static int POINT_RADIUS=3;
    private final static int MARGIN=5;
    private final static float TEXT_SIZE=20;
    private final static int ALPHA=150;
    private final static int BG_ALPHA=125;

    private final AppDensity res;

    private final Paint statusTextPaint;
    private Point tl;

    public Canvas canvas;

    public MapsForgeCanvas(Context context, AppDensity r) {
        res = r;
        statusTextPaint=createTextPaint(context, TEXT_SIZE);
    }

    public void init(Canvas c, Point topLeftPoint) {
        tl = topLeftPoint;
        canvas=c;
    }


    public Paint  createTextPaint(Context context, float size) {
        Paint p=AndroidGraphicFactory.INSTANCE.createPaint();;
        p.setColor(Color.BLACK);

        p.setTextSize(50);


        //p.setTextSize(res.toSDPf(size));
        //p.setFakeBoldText(true);
    //        p.setStyle(Paint.Style.FILL);
        //p.setAntiAlias(true);
        return p;
    }


    public void drawTextTop(String text, int i) {
        final int ts = statusTextPaint.getTextHeight("X")*i;

        canvas.drawText(text, MARGIN, ts, statusTextPaint);

        canvas.drawText("Test", (int)tl.x+500,(int)tl.y+500, statusTextPaint);
        canvas.drawCircle((int)tl.x+5,(int)tl.y+5,10, statusTextPaint);
    }



}