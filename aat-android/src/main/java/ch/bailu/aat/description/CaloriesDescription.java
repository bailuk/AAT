package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.general.SolidWeight;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat_lib.description.LongDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.presets.SolidMET;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;

public class CaloriesDescription extends LongDescription {


    private final Context context;


    public Context getContext() {
        return context;
    }

    public CaloriesDescription(Context context) {
        this.context = context;
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.calories);
    }

    @Override
    public String getUnit() {
        return "kcal";
    }

    public String getValue() {
        return String.valueOf(getCache());
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache((long)calculateCalories(info));
    }


    private float calculateCalories(GpxInformation track) {
        int preset = new SolidPreset(new AndroidSolidDataDirectory(getContext())).getIndex();

        float hours = ((float)track.getTimeDelta()) / (1000f * 60f * 60f);
        float met = new SolidMET(new Storage(getContext()), preset).getMETValue();
        // TODO : add userfeedback about wrong calculation here ?
        float weight = new SolidWeight(getContext()).getValue();
        float kcal = hours*met*weight;

        return kcal;
    }
}
