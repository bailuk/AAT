package ch.bailu.aat.views.graph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import ch.bailu.aat.map.BitmapDraw;
import ch.bailu.aat.map.NodeBitmap;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.UiTheme;

public class GraphPlotter {
    private static final int TEXT_SIZE=15;
    private final int text_size;

    private final Scaler xscaler;
    private final InvertetOffsetScaler yscaler;

    private final Paint paintFont;
    private final Paint paintPlotLines;
    private final Paint paintLines;


    private final Canvas canvas;
    private final BitmapDraw bitmapCanvas = new BitmapDraw();

    private final int width;
    private final int height;

    private Point pointA=new Point(-5,-5), pointB = new Point(-5,-5);

    private final NodeBitmap nodeBitmap;

    public GraphPlotter(Canvas c, int w, int h, float xScale, AppDensity res, UiTheme theme) {
        width=w;
        height=h;

        canvas = c;


        paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setDither(false);
        paintFont.setColor(theme.getGraphTextColor());
        paintFont.setTextSize(res.toPixelScaled_f(TEXT_SIZE));


        paintPlotLines = new Paint();
        paintPlotLines.setAntiAlias(true);
        paintPlotLines.setDither(false);
        paintPlotLines.setStrokeWidth(res.toPixel_f(2));

        paintLines = new Paint();
        paintLines.setAntiAlias(true);
        paintLines.setDither(false);
        paintLines.setStrokeWidth(0);
        paintLines.setColor(theme.getGraphLineColor());


        text_size = Math.max(Math.round(paintFont.getTextSize()), 1);
        xscaler = new Scaler(width, xScale);
        yscaler = new InvertetOffsetScaler(height);

        nodeBitmap = NodeBitmap.get(res);
    }



    public void roundYScale(int roundTo) {
        yscaler.round(roundTo);
    }

    public void inlcudeInYScale(float value) {
        yscaler.addValue(value);
    }
    public void drawYScale(int lines, float factor, boolean drawFirstValue) {
        lines = Math.min(height / (text_size*2), lines);
        lines = Math.max(1, lines);

        float space = yscaler.getRealDistance() / lines;
        space = Math.max(1, space);

        for (float x=yscaler.getRealOffset(); x<= yscaler.getRealTop(); x+=space) {
            drawHorizontalLine(x,factor, drawFirstValue);
        }
    }


    private void drawHorizontalLine(float value, float factor, boolean drawFirstValue) {
        int pixel = (int)yscaler.scale(value);

        drawScaleLine(0, pixel, width, pixel);

        if (drawFirstValue || pixel < height-text_size)
            drawScaleText(0,pixel, String.valueOf((int) (value*factor)) );
    }


    private void drawScaleLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1 , x2, y2,  paintLines);
    }

    private void drawScaleText(int x,int y, String value) {
        int w = value.length()*text_size;
        int h = text_size;

        if ((x+w) > width) x = width - w;
        if ((y-h) < 0) y=h;

        canvas.drawText(value, x, y, paintFont);
    }



    public void drawXScale(int lines, float factor, boolean drawText) {
        lines = Math.min(width / (text_size*4), lines);
        lines = Math.max(1, lines);

        float space=xscaler.getReal()/lines;
        space = Math.max(1, space);

        for (float x=0; x <= xscaler.getReal(); x+=space) {
            drawVerticalLine(x, factor, drawText);
        }

    }

    private void drawVerticalLine(float value, float factor, boolean drawText) {
        int pixel = (int)xscaler.scale(value);

        drawScaleLine(pixel, 0 , pixel, height);

        if (drawText && pixel > text_size) {
            String text = String.valueOf((int) (value * factor));
            drawScaleText(pixel-text_size, height, text);
        }
    }


    public int plotData(float xvalue, float yvalue, int color) {
        int delta;
        scaleToPixel(xvalue, yvalue);

        delta = Math.max(pointA.x - pointB.x, 0);

        if (delta > 1) {
            if (pointB.x < 0) pointB.y=pointA.y;
            plotLine(pointB,pointA,color);
            switchPoints();
        }
        return delta;
    }

    private void scaleToPixel(float xvalue, float yvalue) {
        pointA.x=(int)xscaler.scale(xvalue);
        pointA.y=(int)yscaler.scale(yvalue);
    }


    private void plotLine(Point pA, Point pB, int color) {
        paintPlotLines.setColor(color);
        canvas.drawLine(pA.x, pA.y, pB.x, pB.y, paintPlotLines);
    }

    private void switchPoints() {
        Point t=pointB;
        pointB=pointA;
        pointA=t;
    }

    public void plotPoint(float xvalue, float yvalue, int color) {
        scaleToPixel(xvalue,yvalue);
        bitmapCanvas.draw(canvas, nodeBitmap.getTileBitmap().getAndroidBitmap(), pointA, color);
    }
}
