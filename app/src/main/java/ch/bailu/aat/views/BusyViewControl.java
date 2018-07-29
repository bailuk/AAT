package ch.bailu.aat.views;

import android.view.View;
import android.view.ViewGroup;

public class BusyViewControl implements  BusyInterface{

    public final BusyViewContainer busy;

    private boolean isWaiting = false;



    public BusyViewControl (ViewGroup parent) {
        busy = new BusyViewContainer(parent.getContext());
        parent.addView(busy,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        busy.setVisibility(View.GONE);
    }


    public void startWaiting() {
        isWaiting = true;
        busy.setVisibility(View.VISIBLE);
        busy.bringToFront();
    }

    public void stopWaiting() {
        isWaiting = false;
        busy.setVisibility(View.GONE);
    }

    @Override
    public boolean isWaiting() {
        return isWaiting;
    }


    public void setText(CharSequence t) {
        busy.setText(t);
    }
}
