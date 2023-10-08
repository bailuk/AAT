package ch.bailu.aat_lib.preferences.presets;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.exception.ValidationException;
import ch.bailu.aat_lib.preferences.SolidString;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidMET extends SolidString {
    private final int preset;

    public SolidMET(StorageInterface s, int i) {
        super(s, SolidMET.class.getSimpleName()+ "_" + i);
        preset = i;
    }

    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_met();
    }

    @Nonnull
    @Override
    public String getValueAsString() {
        var result = super.getValueAsString();
        if (getStorage().isDefaultString(result)) {
            result = getDefaultValue();
        }
        return result;
    }

    private String getDefaultValue() {
        var metList =  Res.str().p_met_list();
        if (preset < metList.length) {
            return metList[preset];
        } else {
            return metList[0];
        }
    }

    public float getMETValue() {
        var val = getValueAsString();

        final int from = 0;
        int to = val.indexOf(' ');

        float r = 0f;

        if (to > from) {
            try {
                String met = val.substring(from, to);
                r = Float.parseFloat(met);
            } catch (NumberFormatException e) {
                r = 0f;
            }
        }

        if (r > 20f || r < 0f) {
            r = 0f;
        }
        return r;
    }

    @Override
    public void setValueFromString(String string) throws ValidationException {
        string = string.trim();

        if (! validate(string)) {
            throw new ValidationException(Res.str().error_met());
        } else {
            setValue(string);
        }
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        String[] array = Res.str().p_met_list();
        Collections.addAll(list, array);
        return list;
    }

    @Override
    public boolean validate(String s) {
        // entering 0.0 is still possible
        return s.split(" ")[0].matches("1?[0-9].[0-9]|20.0");
    }
}
