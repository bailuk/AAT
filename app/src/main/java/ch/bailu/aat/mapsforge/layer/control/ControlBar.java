package ch.bailu.aat.mapsforge.layer.control;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.mapsforge.MapsForgeView;
import ch.bailu.aat.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.views.map.OsmInteractiveView;

public class ControlBar extends MapsForgeLayer implements View.OnClickListener {
    public final static int TOP=0;
    public final static int LEFT=1;
    public final static int BOTTOM=2;
    public final static int RIGHT=3;

    private final static int TRANSPARENT = Color.argb(50, 0, 0, 0);

    private final ch.bailu.aat.views.ControlBar bar;

    private final int placement;

    private final MapsForgeView map;
    public static int getOrientation(int placement) {
        if (placement == TOP || placement == BOTTOM) {
            return LinearLayout.HORIZONTAL;
        }
        return LinearLayout.VERTICAL;
    }

    public ControlBar(MapsForgeView v, ch.bailu.aat.views.ControlBar b, int p, int color) {
        map = v;
        placement = p;
        bar=b;
        bar.setBackgroundColor(color);
        bar.setOnClickListener2(this);
        bar.setVisibility(View.GONE);
        map.addView(bar);

    }

    public ControlBar(MapsForgeView v, ch.bailu.aat.views.ControlBar b, int p) {
        this(v,b,p, TRANSPARENT);
    }



    public ch.bailu.aat.views.ControlBar getBar() {
        return bar;
    }


    public boolean isBarVisible() {
        return bar.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onSharedPreferenceChanged(String key) {

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
        }
    }


    public void showBar() {
        if (!isBarVisible()) {
            AppLog.d(this, "show bar");
            bar.setVisibility(View.VISIBLE);
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

        int h = map.getModel().mapViewDimension.getDimension().height;
        int w = map.getModel().mapViewDimension.getDimension().width;
        AppLog.d(this, map.getModel().mapViewDimension.getDimension().toString());
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
    public void onContentUpdated(int iid, GpxInformation info) {

    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

    }
}
