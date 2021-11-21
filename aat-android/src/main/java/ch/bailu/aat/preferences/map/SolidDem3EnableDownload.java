package ch.bailu.aat.preferences.map;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.ToDo;

public class SolidDem3EnableDownload extends SolidBoolean {
    public SolidDem3EnableDownload(StorageInterface storageInterface) {
        super(storageInterface, SolidDem3EnableDownload.class.getSimpleName());
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Enable automatic download");
    }
}
