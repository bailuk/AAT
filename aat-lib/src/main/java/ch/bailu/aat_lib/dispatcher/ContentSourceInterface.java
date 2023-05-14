package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface ContentSourceInterface {
    void setTarget(@Nonnull OnContentUpdatedInterface target);
    void sendUpdate(int iid, @Nonnull GpxInformation info);
    void requestUpdate();
    void onPause();
    void onResume();

    int getIID();
    GpxInformation getInfo();
}
