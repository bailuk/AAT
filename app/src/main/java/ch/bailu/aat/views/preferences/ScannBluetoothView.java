package ch.bailu.aat.views.preferences;

import android.view.View;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.views.AbsLabelTextView;

public class ScannBluetoothView extends AbsLabelTextView implements View.OnClickListener, OnContentUpdatedInterface {
    private final ServiceContext scontext;
    public ScannBluetoothView(ServiceContext s) {
        super(s.getContext(), ToDo.translate("Scann for Bluetooth sensors"));
        scontext = s;
        setText();
        setOnClickListener(this);
    }


    private void setText() {
        new InsideContext(scontext) {
            @Override
            public void run() {
                setText(scontext.getSensorService().toString());
            }
        };
    }


    @Override
    public void onClick(View v) {
        new InsideContext(scontext) {
            @Override
            public void run() {
                scontext.getSensorService().scann();
            }
        };
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (iid == InfoID.SENSORS) {
            setText();
        }

    }
}

