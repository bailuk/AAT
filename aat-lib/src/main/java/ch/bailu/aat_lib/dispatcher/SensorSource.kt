package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface

class SensorSource(private val services: ServicesInterface, private val broadcaster: Broadcaster, private val iid: Int) : ContentSource() {

    private val changedAction: String = AppBroadcaster.SENSOR_CHANGED + iid
    private val onSensorUpdated =
        BroadcastReceiver { _: Array<out String> -> sendUpdate(getIID(), info) }

    override fun requestUpdate() {
        sendUpdate(getIID(), info)
    }

    override fun onPause() {
        broadcaster.unregister(onSensorUpdated)
    }

    override fun onResume() {
        broadcaster.register(changedAction, onSensorUpdated)
    }

    override fun getIID(): Int {
        return iid
    }

    override fun getInfo(): GpxInformation {
        return services.sensorService.getInfo(iid)
    }
}
