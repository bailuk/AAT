package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;

public abstract class ContentSource implements ContentSourceInterface {
    private OnContentUpdatedInterface target = OnContentUpdatedInterface.NULL;

    @Override
    public void setTarget(@Nonnull OnContentUpdatedInterface target) {
        this.target = target;
    }

    @Override
    public void sendUpdate(int iid, @Nonnull GpxInformation info) {
        target.onContentUpdated(iid, info);
    }
}
