package ch.bailu.aat_lib.description;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidWeight;
import ch.bailu.aat_lib.preferences.presets.SolidMET;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.aat_lib.resources.Res;

public class CaloriesDescription extends LongDescription {

    private final StorageInterface storage;

    public CaloriesDescription(StorageInterface s) {
        storage = s;
    }

    @Override
    public String getLabel() {
        return Res.str().calories();
    }

    @Override
    public String getUnit() {
        return "kcal";
    }

    public String getValue() {
        return String.valueOf(getCache());
    }

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        setCache((long)calculateCalories(info));
    }

    private float calculateCalories(GpxInformation track) {
        final int   preset = new SolidPreset(storage).getIndex();
        final float hours  = ((float)track.getTimeDelta()) / (1000f * 60f * 60f);
        final float met    = new SolidMET(storage, preset).getMETValue();
        final float weight = new SolidWeight(storage).getValue();
        final float kcal   = hours * met * weight;

        return kcal;
    }
}
