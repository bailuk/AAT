package ch.bailu.aat_lib.preferences.location;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidProvideAltitude extends SolidAltitude {
    private final static String KEY = "ProvideAltitude";
    public SolidProvideAltitude(StorageInterface s, int unit) {
        super(s, KEY, unit);
    }

    @Override
    public void setValue(int v) {
        getStorage().writeIntegerForce(getKey(), v);
    }

    @Nonnull
    @Override
    public String getLabel() {
        return addUnit(Res.str().p_set_altitude());
    }
}
