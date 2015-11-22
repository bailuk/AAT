package ch.bailu.aat.views;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;


public class BusyIndicator extends ProgressBar implements DescriptionInterface {

    private final int ID;
    
    public BusyIndicator(Context context) {
        this(context, -1);
    }

    public BusyIndicator(Context context, int id) {
        super(context);
        ID = id;
        setIndeterminate(true);
        stopWaiting();
    }


    public void startWaiting() {
        setVisibility(ProgressBar.VISIBLE);

    }

    public void stopWaiting() {
        setVisibility(ProgressBar.INVISIBLE);
    }

    public boolean isWaiting() {
        return isShown();
    }

    public class OnSyncAction extends BroadcastReceiver {
        private final int action;
        OnSyncAction(int a) {
            action=a;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            setVisibility(action);
        }
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        if (info.getID()==ID) {
            if (info.isLoaded()) {
                stopWaiting();
            } else {
                startWaiting();
            }
            
        }
    }
}
