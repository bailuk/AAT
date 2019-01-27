package ch.bailu.aat.views.description;

import android.view.View;

import ch.bailu.aat.description.SensorStateDescription;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppTheme;

public class SensorStateButton extends ColorNumberView implements View.OnClickListener {
    private final ServiceContext scontext;


    public SensorStateButton(ServiceContext c) {
        super(new SensorStateDescription(c.getContext()), AppTheme.bar);
        scontext = c;

        setOnClickListener(this);
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
