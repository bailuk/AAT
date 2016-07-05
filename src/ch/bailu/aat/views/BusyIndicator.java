package ch.bailu.aat.views;

import android.content.Context;
import android.widget.ProgressBar;


public class BusyIndicator extends ProgressBar  {

    public BusyIndicator(Context context) {
        super(context);
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
}
