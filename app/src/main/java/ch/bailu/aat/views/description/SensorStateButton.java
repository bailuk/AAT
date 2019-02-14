package ch.bailu.aat.views.description;

import android.view.View;

import ch.bailu.aat.description.SensorStateDescription;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.preferences.ConnectToSensorsView;

public class SensorStateButton extends NumberView implements View.OnClickListener {
    private final ServiceContext scontext;


    public SensorStateButton(ServiceContext c) {
        super(new SensorStateDescription(c.getContext()), AppTheme.bar);
        scontext = c;

        setOnClickListener(this);

        ToolTip.set(this, ConnectToSensorsView.LABEL);
    }


    @Override
    public void onClick(View v) {
        if (v==this) {
            new InsideContext(scontext) {

                @Override
                public void run() {
                    scontext.getSensorService().updateConnections();
                }
            };

        }
    }
}
