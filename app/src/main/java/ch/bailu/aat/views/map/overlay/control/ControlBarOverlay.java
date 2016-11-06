package ch.bailu.aat.views.map.overlay.control;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import org.osmdroid.views.MapView;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;


public abstract class ControlBarOverlay extends OsmOverlay implements Runnable, OnClickListener{
    public final static int TOP=0;
    public final static int LEFT=1;
    public final static int BOTTOM=2;
    public final static int RIGHT=3;

    private final static int HIDE_TIMER_MILLIS=5000;
    private final static int TRANSPARENT = Color.argb(50, 0, 0, 0);

    private final ControlBar bar;
//    private final Timer hideTimer = new Timer(this, HIDE_TIMER_MILLIS);

    private final int placement;

    public static int getOrientation(int placement) {
        if (placement == TOP || placement == BOTTOM) {
            return LinearLayout.HORIZONTAL;
        }
        return LinearLayout.VERTICAL;
    }

    public ControlBarOverlay(OsmInteractiveView v, ControlBar b, int p, int color) {
        super(v);
        placement = p;
        bar=b;
        bar.setBackgroundColor(color);
        bar.setOnClickListener2(this);
        bar.setVisibility(View.GONE);
        v.addView(bar);

    }
    
    public ControlBarOverlay(OsmInteractiveView v, ControlBar b, int p) {
        this(v,b,p, TRANSPARENT);
    }


 
    public ControlBar getBar() {
        return bar;
    }

    public boolean isVisible() {
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
        }
    }


    public void showBar() {
        if (!isVisible()) {
            AppLog.d(this, "show bar");
            bar.setVisibility(View.VISIBLE);
            onShowBar();
        }

        //hideTimer.close();
        //hideTimer.kick();
    }

    public void onShowBar(){}


    @Override
    public void run() {
        hideBar();
    }

    public void hideBar() {
        if (bar!=null && isVisible()) {
            bar.setVisibility(View.GONE);

            onHideBar();
        }
    }


    public void onHideBar() {}

    @Override
    public void draw(MapPainter painter) {

    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e, MapView map) {
        int size=bar.getControlSize();

        int y=(int)e.getY();
        int x=(int)e.getX();

        if (y < size) {
            topTap();
            
        } else if (y > map.getHeight()-size) {
            bottomTap();
            
        } else if (x < size) {
            leftTab();
            
        } else if (x > map.getWidth()-size){
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
}
