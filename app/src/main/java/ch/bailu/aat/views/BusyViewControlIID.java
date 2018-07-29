package ch.bailu.aat.views;

import android.view.ViewGroup;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;

public class BusyViewControlIID extends BusyViewControl implements OnContentUpdatedInterface {
    private final int IID;

    public BusyViewControlIID(ViewGroup parent, int iid) {
        super(parent);
        IID =iid;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (iid== IID) {
            if (info.isLoaded()) {
                stopWaiting();
            } else {
                startWaiting();
            }

        }
    }
}
