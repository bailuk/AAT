package ch.bailu.aat_lib.preferences.map;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.FocFactory;

public abstract class SolidTileCacheDirectory extends SolidFile {

    public SolidTileCacheDirectory(StorageInterface s, FocFactory focFactory) {
        super(s, SolidTileCacheDirectory.class.getSimpleName(), focFactory);
    }




    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_directory_tiles();
    }


    @Nonnull
    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();

        if (getStorage().isDefaultString(r)) {
            r = getDefaultValue();
            setValue(r);
        }
        return r;
    }


    private String getDefaultValue() {

        ArrayList<String> list = new ArrayList<>(5);

        list = buildSelection(list);
        list.add(getStorage().getDefaultString());

        return list.get(0);
    }

}
