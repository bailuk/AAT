package ch.bailu.aat.map.layer.control;

import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.activities.AbsBackButton;
import ch.bailu.aat.map.To;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public abstract class ControlBarLayer implements MapLayerInterface, View.OnClickListener {
    public final static int TOP=0;
    public final static int LEFT=1;
    public final static int BOTTOM=2;
    public final static int RIGHT=3;

    private final ControlBar bar;

    private final int placement;

    private int w, h;
    public static int getOrientation(int placement) {
        if (placement == TOP || placement == BOTTOM) {
            return LinearLayout.HORIZONTAL;
        }
        return LinearLayout.VERTICAL;
    }

    public ControlBarLayer(MapContext mc, ControlBar b, int p, int color) {
        MapViewInterface map = mc.getMapView();
        placement = p;
        bar=b;
        bar.setBackgroundColor(color);
        bar.setOnClickListener2(this);
        bar.setVisibility(View.GONE);
        To.view(map).addView(bar);


        To.view(map).addView(new AbsBackButton.OnBackPressedListener(b.getContext()) {
            @Override
            public boolean onBackPressed() {
                if (To.view(map).getVisibility() == VISIBLE && isBarVisible()) {
                    hideBar();
                    return true;
                }
                return false;
            }
        });
    }


    public ControlBarLayer(MapContext c, ControlBar b, int p) {
        this(c,b,p, MapColor.MEDIUM);
    }



    public ControlBar getBar() {
        return bar;
    }


    public boolean isBarVisible() {
        return bar.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            w = r - l;
            h = b - t;
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
            AppLayout.fadeIn(bar);
            onShowBar();
        }
    }

    public void onShowBar(){}


    public void hideBar() {
        if (bar!=null && isBarVisible()) {
            AppLayout.fadeOut(bar);
            onHideBar();
        }
    }


    public void onHideBar() {}


    @Override
    public boolean onTap(Point tapXY) {
        int size=bar.getControlSize();

        int y=(int)tapXY.y;
        int x=(int)tapXY.x;

        if (y < size) {
            topTap();

        } else if (y > h - size) {
            bottomTap();

        } else if (x < size) {
            leftTab();

        } else if (x > w-size){
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
    public void drawForeground(MapContext mcontext) {}

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}
}
