package ch.bailu.aat.preferences.location;

import android.content.Context;
import android.view.View;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.views.preferences.SolidTextInputDialog;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat_lib.preferences.location.SolidProvideAltitude;
import ch.bailu.aat_lib.service.sensor.SensorState;

public class AndroidSolidAltitudeUtil {
    public static View requestOnClick(View v) {
        v.setOnClickListener(v1 -> requestValueFromUserIfEnabled(v1.getContext()));
        return v;
    }

    public static void requestValueFromUserIfEnabled(Context c) {
        if (SensorState.isConnected(InfoID.BAROMETER_SENSOR) ||
                new SolidAdjustGpsAltitude(new Storage(c)).isEnabled()) {
            requestValueFromUser(c);
        }
    }

    public static void requestValueFromUser(Context c) {
        new SolidTextInputDialog(c,
                new SolidProvideAltitude(new Storage(c),
                        new SolidUnit(new Storage(c)).getIndex()),
                SolidTextInputDialog.INTEGER_SIGNED);
    }
}
