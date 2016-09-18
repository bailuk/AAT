package ch.bailu.aat.views.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class GraphPlotter {
    private static final int TEXT_SIZE=20;
    private static final int YLABEL_XOFFSET=50;
    private static final int YLABEL_YOFFSET=30;
    
    private final Scaler xscaler;
    private final InvertetOffsetScaler yscaler;
    
    private final Paint paint;
    private final Canvas canvas;
    
    private final int width;
    private final int height;
    
    private Point pointA=new Point(-5,-5), pointB = new Point(-5,-5);
    
    public GraphPlotter(Canvas c, int width, int height, float xScale) {
        this.width=width;
        this.height=height;
        
        canvas = c;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(TEXT_SIZE);
        
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

        paint.setColor(Color.WHITE);        
        canvas.drawText(label,YLABEL_XOFFSET , YLABEL_YOFFSET, paint);

    }

    private void drawHorizontalLine(float value, float factor) {
        int pixel = (int)yscaler.scale(value);
        
        drawScaleLine(0, pixel, width, pixel);
        
        pixel=Math.min(height-TEXT_SIZE, pixel);
        drawScaleText(0,pixel,Color.WHITE, String.valueOf((int) (value*factor)) );
    }
    
    
    private void drawScaleLine(int x1, int y1, int x2, int y2) {
        paint.setColor(Color.DKGRAY);
        canvas.drawLine(x1, y1 , x2, y2,  paint);
    }
        
    private void drawScaleText(int x,int y, int color, String value) {
        if ((x+6) > width) x = width - 30;
            
        if ((y+6) > height) y=height;    
        else if ((y-6) < 0) y=12;
            
        paint.setColor(color);                
        canvas.drawText(value, x, y, paint);
    }
        
        
    
    public void drawXScale(int lines, String label, float factor) {
        int space=(int)xscaler.getReal()/lines;
        space=Math.max(space, 1);
        
        for (float x=0; x <= xscaler.getReal()-10; x+=space) {
            drawVerticalLine(x, factor);
        }

        paint.setColor(Color.WHITE);
        canvas.drawText(label, width/2, height-20, paint);
    }
        
    private void drawVerticalLine(float value, float factor) {
        int pixel = (int)xscaler.scale(value);
            
        drawScaleLine(pixel, 0 , pixel, height);
        drawScaleText(pixel,height, Color.WHITE, String.valueOf((int) (value*factor)));
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
        paint.setStrokeWidth(3);
        paint.setColor(color);
        canvas.drawLine(pA.x, pA.y, pB.x, pB.y, paint);
    }

    private void switchPoints() {
        Point t=pointB;
        pointB=pointA;
        pointA=t;
    }
}
