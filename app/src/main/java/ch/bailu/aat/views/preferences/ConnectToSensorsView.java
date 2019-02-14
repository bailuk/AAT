package ch.bailu.aat.views.preferences;

import android.view.View;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.views.AbsLabelTextView;

public class ConnectToSensorsView extends AbsLabelTextView implements View.OnClickListener, OnContentUpdatedInterface {

    public static final String LABEL = ToDo.translate("Connect to enabled sensors\u2026");

    private String busy="";
    private final ServiceContext scontext;

    public ConnectToSensorsView(ServiceContext s) {
        super(s.getContext(), LABEL);
        scontext = s;
        setText();
        setOnClickListener(this);
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
