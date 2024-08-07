package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.ServicesInterface

class CurrentLocationSource(
    private val services: ServicesInterface,
    private val broadcaster: Broadcaster
) : ContentSource() {
    private val onLocationChange = BroadcastReceiver {
        sendUpdate(
            InfoID.LOCATION,
            services.locationService.locationInformation
        )
    }

    override fun requestUpdate() {
        sendUpdate(InfoID.LOCATION, services.locationService.locationInformation)
    }

    override fun onPause() {
        broadcaster.unregister(onLocationChange)
    }

    override fun onResume() {
        broadcaster.register(AppBroadcaster.LOCATION_CHANGED, onLocationChange)
    }

    override fun getIID(): Int {
        return InfoID.LOCATION
    }

    override fun getInfo(): GpxInformation {
        return services.locationService.locationInformation
    }
}
