package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformationProvider;

public interface ContentSourceInterface extends GpxInformationProvider {
    void setTarget(@Nonnull OnContentUpdatedInterface target);
    void requestUpdate();
    void onPause();
    void onResume();

    int getIID();
}
