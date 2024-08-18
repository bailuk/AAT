package ch.bailu.aat_lib.service.cache;


import java.util.ArrayList;

import ch.bailu.aat_lib.util.Rect;

public final class Span {
    private int deg;
    private int firstPixel;
    private int lastPixel;

    public Span() {
        deg = -1;
        firstPixel = 0;
        lastPixel = -1;
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


    public int pixelDim() {
        if (deg > -1 && lastPixel >= firstPixel) {
            return lastPixel - firstPixel + 1;
        }
        return 0;
    }


    public void incrementAndCopyIntoArray(ArrayList<Span> span_array, int pixel_index, int deg) {
        lastPixel = pixel_index;

        if (deg != this.deg) {
            copyIntoArray(span_array);

            this.deg = deg;
            firstPixel = pixel_index;
        }
    }

    /*
    public void copyIntoArray(ArrayList<Span> span_array, int pixel_index) {
        lastPixel = pixel_index;
        copyIntoArray(span_array);
    }
     */

    public void copyIntoArray(ArrayList<Span> l) {
        if (pixelDim() > 0) l.add(new Span(this));
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
