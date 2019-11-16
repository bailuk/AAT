package ch.bailu.aat.services.cache;

import android.graphics.Rect;

import java.util.ArrayList;

public class Span {
    private int deg;
    private int firstPixel;
    private int lastPixel;

    public Span(int d) {
        deg = d;
        firstPixel = lastPixel = 0;
    }



    public Span(Span s) {
        deg = s.deg;
        firstPixel = s.firstPixel;
        lastPixel = s.lastPixel;
    }


    public int firstPixelIndex() {
        return firstPixel;
    }
    public int lastPixelIndex() {
        return lastPixel;
    }

    public int deg() {
        return deg;
    }


    public int pixelCount() {
        return lastPixel - firstPixel;
    }


    public void copyIntoArray(ArrayList<Span> span_array, int pixel_index, int deg) {
        lastPixel = pixel_index;

        if (deg != this.deg) {
            copyIntoArray(span_array);

            this.deg = deg;
            firstPixel = pixel_index;
        }
    }

    public void copyIntoArray(ArrayList<Span> span_array, int pixel_index) {
        lastPixel = pixel_index;
        copyIntoArray(span_array);
    }

    private void copyIntoArray(ArrayList<Span> l) {
        if (pixelCount() >0) l.add(new Span(this));
    }

    public static Rect toRect(Span laSpan, Span loSpan) {
        Rect r = new Rect();
        r.top=laSpan.firstPixel;
        r.bottom=laSpan.lastPixel;
        r.left=loSpan.firstPixel;
        r.right=loSpan.lastPixel;
        return r;
    }


}