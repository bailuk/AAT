package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.resources.Res

abstract class StateDescription : ContentDescription() {
    private var state = StateID.OFF
    override fun getLabel(): String {
        return "R.string.d_state"
    }

    override fun getValue(): String {
        val result: String = when (state) {
            StateID.NO_ACCESS -> Res.str().gps_noaccess()
            StateID.NO_SERVICE -> Res.str().gps_nogps()
            StateID.ON -> Res.str().on()
            StateID.OFF -> Res.str().off()
            StateID.PAUSE -> Res.str().status_paused()
            StateID.AUTO_PAUSED -> Res.str().status_autopaused()
            else -> Res.str().gps_wait()
        }
        return result
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        state = info.state
    }
}
