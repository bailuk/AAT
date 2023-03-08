package ch.bailu.aat.views.preferences;

import android.view.View;

import javax.annotation.Nonnull;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.LabelTextView;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public class ScanBluetoothView extends LabelTextView implements View.OnClickListener, OnContentUpdatedInterface {
    private final ServiceContext scontext;
    public ScanBluetoothView(ServiceContext s, UiTheme theme) {
        super(s.getContext(), s.getContext().getString(R.string.sensor_scan), theme);
        scontext = s;
        setText();
        setOnClickListener(this);
        theme.button(this);
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
                scontext.getSensorService().scan();
            }
        };
    }

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        if (iid == InfoID.SENSORS) {
            setText();
        }

    }
}
