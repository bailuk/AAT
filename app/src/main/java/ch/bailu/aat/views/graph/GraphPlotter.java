package ch.bailu.aat.views.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import ch.bailu.aat.helpers.AppDensity;

public class GraphPlotter {
    private static final int TEXT_SIZE=15;
    private final int text_size;

    private static final int YLABEL_XOFFSET=50;
    private final int ylabel_xoffset;

    private static final int YLABEL_YOFFSET=30;
    private final int ylabel_yoffset;


    private final Scaler xscaler;
    private final InvertetOffsetScaler yscaler;

    private final Paint paintFont;
    private final Paint paintPlotLines;
    private final Paint paintLines;


    private final Canvas canvas;
    
    private final int width;
    private final int height;
    
    private Point pointA=new Point(-5,-5), pointB = new Point(-5,-5);
    
    public GraphPlotter(Canvas c, int w, int h, float xScale, AppDensity res) {
        width=w;
        height=h;

        canvas = c;


        paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setDither(false);
        paintFont.setColor(Color.WHITE);
        paintFont.setTextSize(res.toSDPf(TEXT_SIZE));


        paintPlotLines = new Paint();
        paintPlotLines.setAntiAlias(true);
        paintPlotLines.setDither(false);
        paintPlotLines.setStrokeWidth(res.toDPf(2));

        paintLines = new Paint();
        paintLines.setAntiAlias(true);
        paintLines.setDither(false);
        paintLines.setStrokeWidth(0);
        paintLines.setColor(Color.GRAY);

        text_size = (int)paintFont.getTextSize();
        ylabel_xoffset = res.toDPi(YLABEL_XOFFSET);
        ylabel_yoffset = res.toDPi(YLABEL_YOFFSET);

        xscaler = new Scaler(width, xScale);
        yscaler = new InvertetOffsetScaler(height);
    }



    public void roundYScale(int roundTo) {
        yscaler.round(roundTo);
    }
    
    public void inlcudeInYScale(float value) {
        yscaler.addValue(value);
    }
    public void drawYScale(int lines, String label, float factor) {
        int space=(int)yscaler.getRealDistance()/lines;
        space=Math.max(space, 1);
        
        for (int x=(int)yscaler.getRealOffset(); x< (int)yscaler.getRealTop(); x+=space) {
            drawHorizontalLine(x,factor);
        }


        canvas.drawText(label,ylabel_xoffset , ylabel_yoffset, paintFont);

    }

    private void drawHorizontalLine(float value, float factor) {
        int pixel = (int)yscaler.scale(value);
        
        drawScaleLine(0, pixel, width, pixel);
        
        pixel=Math.min(height-text_size, pixel);
        drawScaleText(0,pixel, String.valueOf((int) (value*factor)) );
    }
    
    
    private void drawScaleLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1 , x2, y2,  paintLines);
    }
        
    private void drawScaleText(int x,int y, String value) {
        /**/
        int space = text_size/2;

        if ((x+space) > width) x = width - space*5;
            
        if ((y+space) > height) y=height;
        else if ((y-space) < space) y=space*2;
            
        canvas.drawText(value, x, y, paintFont);
    }
        
        
    
    public void drawXScale(int lines, String label, float factor) {
        int space=(int)xscaler.getReal()/lines;
        space=Math.max(space, 1);
        
        for (float x=0; x <= xscaler.getReal()-10; x+=space) {
            drawVerticalLine(x, factor);
        }

        canvas.drawText(label, width/2, height-text_size, paintFont);
    }
        
    private void drawVerticalLine(float value, float factor) {
        int pixel = (int)xscaler.scale(value);
            
        drawScaleLine(pixel, 0 , pixel, height);
        drawScaleText(pixel,height, String.valueOf((int) (value*factor)));
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
}
