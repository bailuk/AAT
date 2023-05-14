package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFile;

public class OverlaySource extends FileSource{
    private final SolidOverlayFile soverlay;
    private boolean enabled = true;

    private final OnPreferencesChanged onPreferencesChanged = new OnPreferencesChanged() {
        @Override
        public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
            if (soverlay.hasKey(key)) {
                initAndUpdateOverlay();
            }
        }
    };

    public OverlaySource(AppContext context, int iidOrIndex) {
        super(context, toIID(iidOrIndex));
        soverlay = new SolidOverlayFile(context.getStorage(), context, toIndex(iidOrIndex));
        soverlay.register(onPreferencesChanged);

        initAndUpdateOverlay();
    }

    public void initAndUpdateOverlay() {
        setFileID(soverlay.getValueAsString());

        if (enabled && soverlay.isEnabled()) {
            super.enable();
        } else {
            super.disable();
        }
    }

    private static int toIndex(int idOrIndex) {
        if (idOrIndex < InfoID.OVERLAY) {
            return idOrIndex;
        }
        return idOrIndex - InfoID.OVERLAY;
    }

    private static int toIID(int idOrIndex) {
        if (idOrIndex >= InfoID.OVERLAY) {
            return idOrIndex;
        }
        return InfoID.OVERLAY + idOrIndex;
    }

    @Override
    public void enable() {
        enabled = true;
        if (soverlay.isEnabled()) {
            super.enable();
        }
    }

    @Override
    public void disable() {
        enabled = false;
        super.disable();
    }
}
