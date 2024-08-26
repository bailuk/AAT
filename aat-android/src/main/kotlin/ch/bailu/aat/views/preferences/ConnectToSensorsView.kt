package ch.bailu.aat.views.preferences

import android.view.View
import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.service.sensor.SensorState

class ConnectToSensorsView(private val scontext: ServiceContext, theme: UiTheme) : LabelTextView(
    scontext.getContext(), scontext.getContext().getString(R.string.sensor_connect), theme
), View.OnClickListener, TargetInterface {
    private var busy = ""

    init {
        setText()
        setOnClickListener(this)
        theme.button(this)
    }

    private fun setText() {
        setText("${SensorState.overviewString} $busy")
    }

    override fun onClick(v: View) {
        scontext.insideContext { scontext.getSensorService().updateConnections() }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (iid == InfoID.SENSORS) {
            busy = if (info.getState() == StateID.WAIT) context.getString(R.string.gps_wait) else ""
            setText()
        }
    }
}
