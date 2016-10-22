package ch.bailu.aat.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppTheme;

public class BusyButton extends ViewGroup   {
    public final int SPACE=16;
    
    private final ImageButton button;
    private final BusyIndicator busy;
    
    public BusyButton(Context context, int image_res) {
        super(context);
        
        button = new ImageButton(context);
        button.setImageResource(image_res);
        button.setClickable(false);
        AppTheme.themify(button);
        addView(button);
        

        busy = new BusyIndicator(context);
        busy.setClickable(false);
        addView(busy);

        stopWaiting();
        setBackgroundResource(R.drawable.button);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            button.layout(0, 0, r-l, b-t);
            busy.layout(SPACE, SPACE, r-SPACE-l, b-SPACE-t);
        }
    }

    
    public void startWaiting() {
        busy.startWaiting();
    }

    
    public void stopWaiting() {
        busy.stopWaiting();
    }

    public boolean isWaiting() {
        return busy.isWaiting();
    }

    


    public BusyControl getBusyControl(int id) {
        return new BusyControl(id);
    }
    
    public class BusyControl implements OnContentUpdatedInterface {
        private final int ID;

        public BusyControl(int iid) {
            ID=iid;
        }
        
        
        @Override
        public void onContentUpdated(GpxInformation info) {
            if (info.getID()==ID) {
                if (info.isLoaded()) {
                    stopWaiting();
                } else {
                    startWaiting();
                }
                
            }
        }
    }
}
