package ch.bailu.aat.views.preferences;

import android.view.View;

import ch.bailu.aat.R;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.sensor.SensorState;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.LabelTextView;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.StateID;

public class ConnectToSensorsView extends LabelTextView implements View.OnClickListener, OnContentUpdatedInterface {

    private String busy="";
    private final ServiceContext scontext;

    public ConnectToSensorsView(ServiceContext s, UiTheme theme) {
        super(s.getContext(), s.getContext().getString(R.string.sensor_connect), theme);
        scontext = s;
        setText();
        setOnClickListener(this);
        theme.button(this);
    }


    private void setText() {
        setText(SensorState.getOverviewString() + " " + busy);
    }


    @Override
    public void onClick(View v) {
        new InsideContext(scontext) {
            @Override
            public void run() {
                scontext.getSensorService().updateConnections();
            }
        };
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (iid == InfoID.SENSORS) {
            if (info.getState() == StateID.WAIT)
                busy = getContext().getString(R.string.gps_wait);
            else busy = "";

            setText();
        }

    }
}
