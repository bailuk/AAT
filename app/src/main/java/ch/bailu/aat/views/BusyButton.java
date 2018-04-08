package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageButton;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ui.AppTheme;

public class BusyButton extends ImageButton implements Runnable {

    private final static int DURATION = 600;
    private final static int PAUSE = 100;


    private final Timer timer = new Timer(this, DURATION+PAUSE);
    private final TransitionDrawable drawable;
    private boolean isWaiting = false;
    private boolean isReversed = false;


    public BusyButton(Context context, int image_res) {
        super(context);
        
        setClickable(false);
        AppTheme.themify(this);


        Drawable white = getResources().getDrawable(image_res);
        Drawable orange = white.getConstantState().newDrawable().mutate();
        orange.setColorFilter(
                AppTheme.getHighlightColor(),
                PorterDuff.Mode.MULTIPLY);

        drawable = new TransitionDrawable(new Drawable[]{white, orange});

        setImageDrawable(drawable);
        setBackgroundResource(R.drawable.button);

        timer.kick();
    }





    public void startWaiting() {
        isWaiting = true;
    }




    public void stopWaiting() {
        isWaiting = false;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    


    public BusyControl getBusyControl(int id) {
        return new BusyControl(id);
    }


    @Override
    public void run() {

        if (isWaiting() || isReversed) {
            drawable.reverseTransition(DURATION);
            isReversed = !isReversed;
        }

        timer.kick();
    }






    public class BusyControl implements OnContentUpdatedInterface {
        private final int ID;

        public BusyControl(int iid) {
            ID=iid;
        }
        
        
        @Override
        public void onContentUpdated(int iid, GpxInformation info) {
            if (iid==ID) {
                if (info.isLoaded()) {
                    stopWaiting();
                } else {
                    startWaiting();
                }
                
            }
        }
    }
}
