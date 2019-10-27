package ch.bailu.aat.views.description;

import ch.bailu.aat.description.SensorStateDescription;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppTheme;

public class SensorStateButton extends NumberView {

    public SensorStateButton(ServiceContext c) {
        super(new SensorStateDescription(c.getContext()), AppTheme.bar);
        requestOnClickSensorReconect();
    }
}
