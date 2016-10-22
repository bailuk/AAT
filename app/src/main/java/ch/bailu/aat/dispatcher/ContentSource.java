package ch.bailu.aat.dispatcher;

import ch.bailu.aat.gpx.GpxInformation;

public abstract class ContentSource  {
    private OnContentUpdatedInterface target = OnContentUpdatedInterface.NULL;

    public void add(OnContentUpdatedInterface t) {
        target = t;
    }

    public abstract void requestUpdate();

    public void sendUpdate(GpxInformation info) {
        target.onContentUpdated(info);
    }

    
    public abstract void onPause();
    public abstract void onResume();

}
