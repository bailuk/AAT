package ch.bailu.aat_lib.preferences.system;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.foc.FocFactory;

public abstract class SolidDataDirectoryDefault extends SolidFile {

    public SolidDataDirectoryDefault(StorageInterface s, FocFactory focFactory) {
        super(s, SolidDataDirectoryDefault.class.getSimpleName(),focFactory);
    }

    @Nonnull
    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();

        if (getStorage().isDefaultString(r)) {
            return setDefaultValue();
        }

        return r;
    }

    public String setDefaultValue() {
        String r = getDefaultValue();
        setValue(r);
        return r;
    }

    private String getDefaultValue() {
        ArrayList<String> list = new ArrayList<>(5);
        list = buildSelection(list);
        list.add(getStorage().getDefaultString());  // failsave
        return list.get(0);
    }
}
