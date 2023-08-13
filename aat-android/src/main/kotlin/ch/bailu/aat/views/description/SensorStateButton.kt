package ch.bailu.aat.views.description

import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat_lib.description.SensorStateDescription

class SensorStateButton(context: ServiceContext) :
    NumberView(context.getContext(), SensorStateDescription(), AppTheme.bar) {

    init {
        requestOnClickSensorReconnect()
    }
}
