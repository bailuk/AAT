package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.util.Point;

public class GraphPlotter {
    private final int text_size;

    private final Scaler xscaler;
    private final InvertetOffsetScaler yscaler;

    private final GraphCanvas canvas;

    private final int width;
    private final int height;

    private Point pointA=new Point(-5,-5), pointB = new Point(-5,-5);


    public GraphPlotter(GraphCanvas c, int w, int h, float xScale) {
        width=w;
        height=h;
        canvas = c;

        text_size = canvas.getTextSize();
        xscaler = new Scaler(width, xScale);
        yscaler = new InvertetOffsetScaler(height);

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

        canvas.drawLine(0, pixel, width, pixel);

        if (drawFirstValue || pixel < height-text_size)
            drawScaleText(0,pixel, String.valueOf((int) (value*factor)) );
    }

    private void drawScaleText(int x,int y, String value) {
        int w = value.length()*text_size;
        int h = text_size;

        if ((x+w) > width) x = width - w;
        if ((y-h) < 0) y=h;

        canvas.drawText(value, x, y);
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

        canvas.drawLine(pixel, 0 , pixel, height);

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
            canvas.drawLine(pointB, pointA, color);
            switchPoints();
        }
        return delta;
    }

    private void scaleToPixel(float xvalue, float yvalue) {
        pointA.x=(int)xscaler.scale(xvalue);
        pointA.y=(int)yscaler.scale(yvalue);
    }

    private void switchPoints() {
        Point t=pointB;
        pointB=pointA;
        pointA=t;
    }

    public void plotPoint(float xvalue, float yvalue, int color) {
        scaleToPixel(xvalue,yvalue);
        canvas.drawBitmap(pointA, color);
    }
}
