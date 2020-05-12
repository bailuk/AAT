package ch.bailu.aat.preferences.location;

import android.content.Context;
import android.view.View;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat.views.preferences.SolidTextInputDialog;

public class SolidProvideAltitude extends SolidAltitude {
    private final static String KEY = "ProvideAltitude";


    public SolidProvideAltitude(Context c, int unit) {
        super(c, KEY, unit);
    }


    @Override
    public void setValue(int v) {
        getStorage().writeIntegerForce(getKey(), v);
    }


    @Override
    public String getLabel() {
        return addUnit(getString(R.string.p_set_altitude));
    }


    public static View requestOnClick(View v) {
        v.setOnClickListener(v1 -> requestValueFromUserIfEnabled(v1.getContext()));
        return v;
    }


    public static void requestValueFromUserIfEnabled(Context c) {
        if (SensorState.isConnected(InfoID.BAROMETER_SENSOR) ||
                new SolidAdjustGpsAltitude(c).isEnabled()) {
            reqeustValueFromUser(c);
        }

    }

    public static void reqeustValueFromUser(Context c) {
        new SolidTextInputDialog(
                new SolidProvideAltitude(c,
                        new SolidUnit(c).getIndex()),
                SolidTextInputDialog.INTEGER_SIGNED);

    }
}
