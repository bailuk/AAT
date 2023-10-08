package ch.bailu.aat_lib.preferences.map;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidDem3EnableDownload extends SolidBoolean {
    public SolidDem3EnableDownload(StorageInterface storageInterface) {
        super(storageInterface, SolidDem3EnableDownload.class.getSimpleName());
    }

    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_dem_auto_download();
    }
}
