package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlay;
import ch.bailu.aat_lib.preferences.map.SolidOverlayInterface;
import ch.bailu.aat_lib.preferences.map.SolidPoiOverlay;

public class OverlaySource extends FileSource {
    private final SolidOverlayInterface soverlay;

    private final OnPreferencesChanged onPreferencesChanged = new OnPreferencesChanged() {
        @Override
        public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
            if (soverlay.hasKey(key)) {
                initAndUpdateOverlay();
            }
        }
    };

    public static OverlaySource factoryPoiOverlaySource(AppContext context) {
        return new OverlaySource(context, new SolidPoiOverlay(context.getDataDirectory()));
    }

    public static OverlaySource factoryCustomOverlaySource(AppContext context, int index) {
        return new OverlaySource(context, new SolidCustomOverlay(context.getStorage(), context, InfoID.OVERLAY + index));
    }

    private OverlaySource(AppContext context, SolidOverlayInterface soverlay) {
        super(context, soverlay.getIID());
        this.soverlay = soverlay;
        initAndUpdateOverlay();
    }

    public void initAndUpdateOverlay() {
        setFile(soverlay.getValueAsFile());
        super.setEnabled(soverlay.isEnabled());
    }

    @Override
    public void setEnabled(boolean enabled) {
        soverlay.setEnabled(enabled);
        super.setEnabled(soverlay.isEnabled());
    }

    @Override
    public void onPause() {
        super.onPause();
        soverlay.unregister(onPreferencesChanged);
    }

    @Override
    public void onResume() {
        super.onResume();
        soverlay.register(onPreferencesChanged);
        initAndUpdateOverlay();
    }
}
