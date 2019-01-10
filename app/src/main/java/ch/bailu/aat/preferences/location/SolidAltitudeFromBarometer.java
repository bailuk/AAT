package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.services.location.Barometer;
import ch.bailu.aat.util.ToDo;

public class SolidAltitudeFromBarometer extends SolidBoolean {

    public static final String KEY = "UseBarometerForAltitude";

    public SolidAltitudeFromBarometer(Context c) {
        super(c, KEY);
    }


    @Override
    public String getLabel() {
        return ToDo.translate("Altitude from barometer");
    }


    @Override
    public String getToolTip() {
        String tip = new Barometer(getContext()).getSensorID();

        if (tip == null) {
            tip =  ToDo.translate("No sensor found");
        }
        return tip;
    }


    public boolean useBarometer() {
        return isEnabled() && new Barometer(getContext()).haveSensor();
    }
}
