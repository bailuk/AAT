package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList;


public class OverlaysSource implements ContentSourceInterface {

    private final OverlaySource[] overlays = new OverlaySource[SolidOverlayFileList.MAX_OVERLAYS];

    public OverlaysSource(AppContext context) {
        for (int i = 0; i<SolidOverlayFileList.MAX_OVERLAYS; i++) {
            overlays[i] = new OverlaySource(context, i);
        }
    }

    @Override
    public void onPause() {
        for (OverlaySource overlaySource : overlays) {
            overlaySource.onPause();
        }
    }

    @Override
    public void onResume() {
        for (OverlaySource overlaySource : overlays) {
            overlaySource.onResume();
        }
    }

    @Override
    public int getIID() {
        return InfoID.OVERLAY;
    }

    @Override
    public GpxInformation getInfo() {
        return GpxInformation.NULL;
    }

    @Override
    public void setTarget(@Nonnull OnContentUpdatedInterface target) {
        for (OverlaySource overlaySource : overlays) {
            overlaySource.setTarget(target);
        }
    }

    @Override
    public void sendUpdate(int iid, @Nonnull GpxInformation info) {}

    @Override
    public void requestUpdate() {
        for (OverlaySource overlaySource : overlays) {
            overlaySource.requestUpdate();
        }
    }
}
