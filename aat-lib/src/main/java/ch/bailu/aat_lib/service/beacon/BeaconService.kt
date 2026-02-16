package ch.bailu.aat_lib.service.beacon

import beacon.BeaconClient
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.beacon.SolidBeaconEnabled
import ch.bailu.aat_lib.preferences.beacon.SolidBeaconServer
import ch.bailu.aat_lib.preferences.beacon.SolidBeaconKey
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.util.WithStatusText
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketException

/**
 * Integrate the #BeaconClient into AAT as a #VirtualService.
 */
class BeaconService(
    private val services: ServicesInterface,
    private val broadcaster: Broadcaster,
    private val storage: StorageInterface
) : VirtualService(), WithStatusText, BeaconServiceInterface, OnPreferencesChanged {
    private var client: BeaconClient? = null
    private var location: GpxInformation = GpxInformation.NULL

    private val onLocation = BroadcastReceiver { submit() }

    init {
        broadcaster.register(AppBroadcaster.LOCATION_CHANGED, onLocation)
        storage.register(this)
        maybeCreateClient(storage)
    }

    @Synchronized
    override fun close() {
        storage.unregister(this)
        broadcaster.unregister(onLocation)
        client?.close()
        client = null
    }

    @Synchronized
    private fun maybeCreateClient(storage: StorageInterface) {
        client?.close()
        client = null

        if (!SolidBeaconEnabled(storage).isEnabled) {
            AppLog.d(this, "Beacon is disabled")
            return
        }

        val server = SolidBeaconServer(storage).getValue()
        val key = SolidBeaconKey(storage).getValue()

        if (server == null || key.toLong() == 0L) {
            AppLog.w(this, "Beacon configuration incomplete")
            return
        }

        try {
            client = BeaconClient(server, key.toLong())
            AppLog.i(this, "Beacon initialized, submitting to " + server)
        } catch (e: SocketException) {
            AppLog.e(this, e, "Beacon initialization failed")
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (key.startsWith("BEACON_")) {
            AppLog.d(this, "Beacon preferences changed")
            maybeCreateClient(storage)
        }
    }

    private fun submit(gpxLocation: GpxInformation) {
        client?.let { beaconClient ->
            services.getBackgroundService().process(BeaconSubmitTask(beaconClient, gpxLocation))
        }
    }

    private fun submit() {
        val locationService = services.getLocationService()
        val newLocation = locationService.getLoggableLocationOrNull(location)
        if (newLocation != null) {
            location = newLocation
            submit(location)
        }
    }

    private class BeaconSubmitTask(
        private val beaconClient: BeaconClient,
        private val gpxLocation: GpxInformation
    ) : BackgroundTask() {
        override fun bgOnProcess(appContext: AppContext): Long {
            try {
                beaconClient.sendFix(gpxLocation.getLatitude(), gpxLocation.getLongitude())
            } catch (e: IOException) {
                // Ignore IOException - beacon submission failed
            }
            return 0 // No size to report for network operations
        }
    }

    override fun appendStatusText(builder: StringBuilder) {
        builder.append("<p>Beacon!</p>")
    }

    override fun getLoggerInformation(): GpxInformation {
        return location
    }
}
