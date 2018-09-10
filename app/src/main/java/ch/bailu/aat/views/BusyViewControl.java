package ch.bailu.aat.views;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

public class BusyViewControl {

    private static final int DEFAULT_ID=-1;

    public final BusyViewContainer busy;
    private SparseBooleanArray isWaiting = new SparseBooleanArray(5);



    public BusyViewControl (ViewGroup parent) {
        busy = new BusyViewContainer(parent.getContext());
        parent.addView(busy,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        busy.setVisibility(View.GONE);
    }

    public void startWaiting() {
        startWaiting(DEFAULT_ID);
    }

    public void stopWaiting() {
        stopWaiting(DEFAULT_ID);
    }

    public void startWaiting(int id) {
        changeWaiting(id, true);
    }

    public void stopWaiting(int id) {
        changeWaiting(id, false);
    }


    private void changeWaiting(int id, boolean w) {
        isWaiting.put(id, w);

        if (isWaiting()) {
            busy.setVisibility(View.VISIBLE);
            busy.bringToFront();
        } else {
            busy.setVisibility(View.GONE);
        }

    }

    public boolean isWaiting() {
        for (int i=0; i<isWaiting.size(); i++) {
            if (isWaiting.valueAt(i)) {
                return true;
            }
        }
        return false;
    }



    public void setText(CharSequence t) {
        busy.setText(t);
    }
}
