package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;

public class BusyViewContainer extends FrameLayout {

    public static final int CENTER=0, TOP_LEFT=1, TOP_RIGHT=2, BOTTOM_LEFT=3, BOTTOM_RIGHT=4;


    private final BusyView busy;

    private int orientation=BOTTOM_RIGHT;



    public BusyViewContainer(Context context) {
        super(context);
        busy = new BusyView(context);
        addView(busy,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setOrientation(int i) {
        orientation=i;
    }


    public void setText(CharSequence t) {
        busy.setText(t);
    }


    @Override
    protected void onLayout(boolean c, int l, int t, int r, int b) {
        super.onLayout(c, l, t, r, b);
        if (orientation == TOP_LEFT) topLeft(l, t, r, b);
        else if (orientation == TOP_RIGHT) topRight(l, t, r, b);
        else if (orientation == BOTTOM_LEFT) bottomLeft(l, t, r, b);
        else if (orientation == BOTTOM_RIGHT) bottomRight(l, t, r, b);
        else center(l, t, r, b);
    }


    private void center(int l, int t, int r, int b) {
        int bw = busy.getWidth();
        int bh = busy.getHeight();
        int w = r - l;
        int h = b - t;

        int nl = w/2 - bw/2;
        int nt = h/2 - bh/2;

        busy.layout(nl, nt, nl + bw, nt + bh);
    }


    private void topLeft(int l, int t, int r, int b) {
        int bw = busy.getWidth();
        int bh = busy.getHeight();


        int nl = 0;
        int nt = 0;

        busy.layout(nl, nt, nl + bw, nt + bh);

    }

    private void topRight(int l, int t, int r, int b) {
        int bw = busy.getWidth();
        int bh = busy.getHeight();
        int w = r - l;

        int nl = w - bw;
        int nt = 0;

        busy.layout(nl, nt, nl + bw, nt + bh);

    }


    private void bottomLeft(int l, int t, int r, int b) {
        int bw = busy.getWidth();
        int bh = busy.getHeight();
        int h = b - t;

        int nl = 0;
        int nt = h - bh;

        busy.layout(nl, nt, nl + bw, nt + bh);

    }

    private void bottomRight(int l, int t, int r, int b) {
        int bw = busy.getWidth();
        int bh = busy.getHeight();
        int w = r - l;
        int h = b - t;

        int nl = w - bw;
        int nt = h - bh;;

        busy.layout(nl, nt, nl + bw, nt + bh);
    }

}
