package ch.bailu.aat.preferences.location;

import android.content.Context;
import android.view.View;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat_lib.preferences.location.SolidAltitude;
import ch.bailu.aat_lib.service.sensor.SensorState;
import ch.bailu.aat.views.preferences.SolidTextInputDialog;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
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


    @Override
    public String getLabel() {
        return addUnit(Res.str().p_set_altitude());
    }


    public static View requestOnClick(View v) {
        v.setOnClickListener(v1 -> requestValueFromUserIfEnabled(v1.getContext()));
        return v;
    }


    public static void requestValueFromUserIfEnabled(Context c) {
        if (SensorState.isConnected(InfoID.BAROMETER_SENSOR) ||
                new SolidAdjustGpsAltitude(new Storage(c)).isEnabled()) {
            reqeustValueFromUser(c);
        }

    }

    public static void reqeustValueFromUser(Context c) {
        new SolidTextInputDialog(c,
                new SolidProvideAltitude(new Storage(c),
                        new SolidUnit(new Storage(c)).getIndex()),
                SolidTextInputDialog.INTEGER_SIGNED);

    }
}
