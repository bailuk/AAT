package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.ServicesInterface

class CurrentLocationSource(
    private val services: ServicesInterface,
    private val broadcaster: Broadcaster
) : SourceInterface {

    private var target = TargetInterface.NULL
    private val onLocationChange = BroadcastReceiver { requestUpdate() }

    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    override fun requestUpdate() {
        target.onContentUpdated(InfoID.LOCATION, services.getLocationService().getLocationInformation())
    }

    override fun onPauseWithService() {
        broadcaster.unregister(onLocationChange)
    }

    override fun onDestroy() {}

    override fun onResumeWithService() {
        broadcaster.register(AppBroadcaster.LOCATION_CHANGED, onLocationChange)
    }

    override fun getIID(): Int {
        return InfoID.LOCATION
    }

    override fun getInfo(): GpxInformation {
        return services.getLocationService().getLocationInformation()
    }
}
