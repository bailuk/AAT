package ch.bailu.aat.map.layer.control;

import android.view.View;
import android.widget.LinearLayout;

import javax.annotation.Nonnull;

import ch.bailu.aat.activities.AbsBackButton;
import ch.bailu.aat.map.To;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.map.edge.Position;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.util.Point;

public abstract class ControlBarLayer implements MapLayerInterface, View.OnClickListener {
    private final ControlBar bar;

    private final Position placement;

    private int w, h;
    public static int getOrientation(Position placement) {
        if (placement == Position.TOP || placement == Position.BOTTOM) {
            return LinearLayout.HORIZONTAL;
        }
        return LinearLayout.VERTICAL;
    }

    public ControlBarLayer(MapContext mc, ControlBar b, Position p, int color) {
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


    public ControlBarLayer(MapContext c, ControlBar b, Position p) {
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


            if (placement == Position.TOP) {
                bar.place(0, 0, w);
            } else if (placement == Position.LEFT) {
                bar.place(0, 0, h);
            } else if (placement == Position.BOTTOM) {

                bar.place(0, h - cs, w);
            } else if (placement == Position.RIGHT) {
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

        int y = tapXY.y;
        int x = tapXY.x;

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
        showHideBar(Position.TOP); }
    public void bottomTap(){
        showHideBar(Position.BOTTOM);}
    public void middleTap(){hideBar();}
    public void rightTab() {
        showHideBar(Position.RIGHT);}
    public void leftTab()  {
        showHideBar(Position.LEFT);}

    private void showHideBar(Position p) {
        if (p == placement) showBar();
        else hideBar();
    }

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {}
}
