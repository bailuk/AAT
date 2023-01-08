package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;

public abstract class ContentSource  {
    private OnContentUpdatedInterface target = OnContentUpdatedInterface.NULL;

    public void setTarget(@Nonnull OnContentUpdatedInterface target) {
        this.target = target;
    }

    public abstract void requestUpdate();

    public void sendUpdate(int iid, GpxInformation info) {
        target.onContentUpdated(iid, info);
    }

    public abstract void onPause();
    public abstract void onResume();

    public abstract int getIID();
    public abstract GpxInformation getInfo();
}
