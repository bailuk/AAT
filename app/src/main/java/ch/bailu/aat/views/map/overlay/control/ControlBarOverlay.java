package ch.bailu.aat.views.map.overlay.control;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import org.osmdroid.views.MapView;

import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;


public abstract class ControlBarOverlay extends OsmOverlay implements Runnable, OnClickListener{
    private final static int HIDE_TIMER_MILLIS=5000;
    private final static int TRANSPARENT = Color.argb(50, 0, 0, 0);

    private final ControlBar bar;
    private boolean isVisible=false;
    private final Timer hideTimer = new Timer(this, HIDE_TIMER_MILLIS);

    public ControlBarOverlay(OsmInteractiveView v, ControlBar b, int color) {
        super(v);
        bar=b;
        bar.setBackgroundColor(color);
        bar.setOnClickListener2(this);
    }
    
    public ControlBarOverlay(OsmInteractiveView v, ControlBar b) {
        this(v,b,TRANSPARENT);
    }


 
    public ControlBar getBar() {
        return bar;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public abstract void showBar();


    public void showBarAtTop() {
        int left=0;
        int top=0;
        
        
        showBar(left, top, getOsmView().getWidth());
    }

    public void showBarAtRight() {
        int left=getMapView().getWidth()-bar.getControlSize();
        int top=0;

        showBar(left, top, getOsmView().getHeight());
    }

    
    public void showBarAtLeft() {
        int left=0;
        int top=0;

        showBar(left, top, getOsmView().getHeight());
    }

    
    public void showBarAtBottom() {
        
        int left=0;
        int top=getMapView().getHeight()-bar.getControlSize();
        showBar(left,top, getOsmView().getWidth());
    }


    private void showBar(int left, int top, int length) {
        if (length < 5) length = 1000;
        
        if (bar!=null && !isVisible && getMapView() != null) {
            getOsmView().addView(bar);
            bar.place(left, top, length);
            isVisible=true;
        }

        hideTimer.close();
        hideTimer.kick();
    }


    @Override
    public void run() {
        hideBar();
    }

    public void hideBar() {
        if (bar!=null && isVisible) {
            getOsmView().removeView(bar);
            isVisible=false;
        }
    }

    @Override
    public void draw(MapPainter painter) {}


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



    public void topTap()   {hideBar();}
    public void bottomTap(){hideBar();}
    public void middleTap(){hideBar();}
    public void rightTab() {hideBar();}
    public void leftTab()  {hideBar();}
}
