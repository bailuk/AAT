package ch.bailu.aat_lib.preferences;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.description.FF;

public class SolidDate extends SolidLong {
    private final String label;

    public SolidDate(StorageInterface s, String k, String l) {
        super(s, k);
        label = l;
    }




    @Override
    public long getValue() {
        if (super.getValue() == 0) {
            return System.currentTimeMillis();
        }
        return super.getValue();
    }


    @Nonnull
    @Override
    public String getValueAsString() {
        return FF.f().LOCAL_DATE.format(getValue());
    }


    @Nonnull
    @Override
    public String getLabel() {
        return label;
    }
}
