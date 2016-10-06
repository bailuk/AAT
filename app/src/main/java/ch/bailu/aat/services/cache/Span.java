package ch.bailu.aat.services.cache;

import android.graphics.Rect;

import java.util.ArrayList;

public class Span {
    private int _deg;
    private int _start;
    private int _end;

    public Span(int d) {
        _deg = d; 
        _start = _end = 0;
    }
    
    

    public Span(Span s) {
        _deg = s._deg;
        _start = s._start;
        _end = s._end;
    }

    
    public int start() {
        return _start;
    }
    
    
    public int end() {
        return _end;
    }
    
    public int deg() {
        return _deg;
    }
    
    
    public int size() {
        return _end - _start;
    }

    
    public void takeSpan(ArrayList<Span> span_array, int pixel_index, int deg) {
        _end = pixel_index;

        if (deg != _deg) {
            takeSpan(span_array);

            _deg = deg;
            _start=pixel_index;
        }
    }

    public void takeSpan(ArrayList<Span> span_array, int pixel_index) {
        _end = pixel_index;
        takeSpan(span_array);
    }

    private void takeSpan(ArrayList<Span> l) {
        if (size() >0) l.add(new Span(this));
    }


    public static Rect toRect(Span laSpan, Span loSpan) {
        Rect r = new Rect();
        r.top=laSpan._start;
        r.bottom=laSpan._end;
        r.left=loSpan._start;
        r.right=loSpan._end;
        return r;
    }
}