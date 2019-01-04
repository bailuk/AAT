package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.SolidMET;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidWeight;

public class CaloriesDescription extends LongDescription{

    
    public CaloriesDescription(Context context) {
        super(context);
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
        int preset = new SolidPreset(getContext()).getIndex();
        
        float hours = ((float)track.getTimeDelta()) / (1000f * 60f * 60f);
        float met = new SolidMET(getContext(), preset).getMETValue();
        // TODO : add userfeedback about wrong calculation here ?
        float weight = new SolidWeight(getContext()).getValue();
        float kcal = hours*met*weight;
        
        return kcal;
    }
}
