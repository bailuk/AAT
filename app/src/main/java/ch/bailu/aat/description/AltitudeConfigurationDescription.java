package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat.preferences.location.SolidAltitudeFromBarometer;
import ch.bailu.aat.util.ToDo;

public class AltitudeConfigurationDescription extends AltitudeDescription {

    private final String configuration;

    public AltitudeConfigurationDescription(Context context) {
        super(context);

        if (new SolidAltitudeFromBarometer(context).useBarometer()) {
            configuration  = ToDo.translate(" (Barometer)");

        } else if (new SolidAdjustGpsAltitude(context).isEnabled()) {
            configuration = " (GPS+-)";

        } else {
            configuration = " (GPS)";
        }
    }


    @Override
    public String getLabel() {
        return super.getLabel() + configuration;
    }
}
