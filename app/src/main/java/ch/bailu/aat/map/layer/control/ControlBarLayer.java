package ch.bailu.aat.map.layer.control;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.util.ui.AppLog;

public abstract class ControlBarLayer implements MapLayerInterface, View.OnClickListener {
    public final static int TOP=0;
    public final static int LEFT=1;
    public final static int BOTTOM=2;
    public final static int RIGHT=3;

    private final static int TRANSPARENT = Color.argb(50, 0, 0, 0);

    private final ch.bailu.aat.views.ControlBar bar;

    private final int placement;

    private final MapViewInterface map;
    private final MapContext mcontext;

    public static int getOrientation(int placement) {
        if (placement == TOP || placement == BOTTOM) {
            return LinearLayout.HORIZONTAL;
        }
        return LinearLayout.VERTICAL;
    }

    public ControlBarLayer(MapContext mc, ch.bailu.aat.views.ControlBar b, int p, int color) {
        map = mc.getMapView();
        mcontext = mc;
        placement = p;
        bar=b;
        bar.setBackgroundColor(color);
        bar.setOnClickListener2(this);
        bar.setVisibility(View.GONE);
        map.addView(bar);

    }

    public ControlBarLayer(MapContext c, ch.bailu.aat.views.ControlBar b, int p) {
        this(c,b,p, TRANSPARENT);
    }



    public ch.bailu.aat.views.ControlBar getBar() {
        return bar;
    }


    public boolean isBarVisible() {
        return bar.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            final int w = r - l;
            final int h = b - t;
            final int cs = bar.getControlSize();


            if (placement == TOP) {
                bar.place(0, 0, w);
            } else if (placement == LEFT) {
                bar.place(0, 0, h);
            } else if (placement == BOTTOM) {

                bar.place(0, h - cs, w);
            } else if (placement == RIGHT) {
                bar.place(w - cs, 0, h);
            }

            AppLog.d(this, "p: " + placement);
            AppLog.d(this, "w1: " + w + ", w2:" + mcontext.getMetrics().getWidth());
            AppLog.d(this, "w: " + bar.getWidth() + ", h: " + bar.getHeight());

            //AppLog.d(this, "w1: " + w + ", w2:" + mcontext.getMetrics().getWidth());
        }
    }


    public void showBar() {
        if (!isBarVisible()) {
            AppLog.d(this, "show bar");
            bar.setVisibility(View.VISIBLE);
            AppLog.d(this, "w: " + bar.getWidth() + ", h: " + bar.getHeight() );
            //AppLog.d(this, "x: " + bar.getX() + ", y: " + bar.getY());
            onShowBar();
        }
    }

    public void onShowBar(){}


    public void hideBar() {
        if (bar!=null && isBarVisible()) {
            bar.setVisibility(View.GONE);

            onHideBar();
        }
    }


    public void onHideBar() {}



    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        AppLog.d(this, "onTap() " +tapXY.toString());
        int size=bar.getControlSize();

        int y=(int)tapXY.y;
        int x=(int)tapXY.x;;

        int h = mcontext.getMetrics().getHeight();
        int w = mcontext.getMetrics().getWidth();

        if (y < size) {
            AppLog.d(this, "top");
            topTap();

        } else if (y > h - size) {
            AppLog.d(this, "bottom");
            bottomTap();

        } else if (x < size) {
            AppLog.d(this, "left");
            leftTab();

        } else if (x > w-size){
            AppLog.d(this, "right");
            rightTab();

        } else {
            middleTap();

        }

        return false;
    }


    public void onClick(View v) {
        showBar();
    }



    public void topTap()   {
        showHideBar(TOP); }
    public void bottomTap(){
        showHideBar(BOTTOM);}
    public void middleTap(){hideBar();}
    public void rightTab() {
        showHideBar(RIGHT);}
    public void leftTab()  {
        showHideBar(LEFT);}

    private void showHideBar(int p) {
        if (p==placement) showBar();
        else hideBar();
    }







    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
