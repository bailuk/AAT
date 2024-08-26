package ch.bailu.aat.views.preferences

import android.view.View
import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.layout.LabelTextView
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID

class ScanBluetoothView(private val scontext: ServiceContext, theme: UiTheme) : LabelTextView(
    scontext.getContext(), scontext.getContext().getString(R.string.sensor_scan), theme
), View.OnClickListener, TargetInterface {
    init {
        setText()
        setOnClickListener(this)
        theme.button(this)
    }

    private fun setText() {
        scontext.insideContext { setText(scontext.getSensorService().toString()) }
    }

    override fun onClick(view: View) {
        scontext.insideContext { scontext.getSensorService().scan() }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (iid == InfoID.SENSORS) {
            setText()
        }
    }
}
