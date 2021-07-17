package ch.bailu.aat.views;

import android.view.ViewGroup;

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;

public class BusyViewControlIID extends BusyViewControl implements OnContentUpdatedInterface {

    public BusyViewControlIID(ViewGroup parent) {
        super(parent);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (info.isLoaded())
            stopWaiting(iid);

        else
            startWaiting(iid);

    }


}
