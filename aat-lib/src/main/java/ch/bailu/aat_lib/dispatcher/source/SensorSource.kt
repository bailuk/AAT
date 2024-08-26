package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface

class SensorSource(private val services: ServicesInterface, private val broadcaster: Broadcaster, private val iid: Int) :
    SourceInterface {

    private var target = TargetInterface.NULL

    private val changedAction: String = AppBroadcaster.SENSOR_CHANGED + iid
    private val onSensorUpdated = BroadcastReceiver { requestUpdate() }

    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    override fun requestUpdate() {
        target.onContentUpdated(getIID(), info)
    }

    override fun onPauseWithService() {
        broadcaster.unregister(onSensorUpdated)
    }

    override fun onDestroy() {}

    override fun onResumeWithService() {
        broadcaster.register(changedAction, onSensorUpdated)
    }

    override fun getIID(): Int {
        return iid
    }

    override fun getInfo(): GpxInformation {
        return services.getSensorService().getInfo(iid)
    }
}
