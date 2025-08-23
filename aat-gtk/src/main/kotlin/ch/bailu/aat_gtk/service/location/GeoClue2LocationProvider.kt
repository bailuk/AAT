package ch.bailu.aat_gtk.service.location

import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.gpx.information.StateID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.location.LocationStackChainedItem
import ch.bailu.aat_lib.service.location.LocationStackItem
import ch.bailu.gtk.geoclue.AccuracyLevel
import ch.bailu.gtk.geoclue.ClientProxy
import ch.bailu.gtk.geoclue.Location
import ch.bailu.gtk.geoclue.Simple
import ch.bailu.gtk.gio.AsyncResult
import ch.bailu.gtk.type.Str


class GeoClue2LocationProvider(item: LocationStackItem) : LocationStackChainedItem(item) {

    private var client: ClientProxy? = null

    private fun updateStateAndLocation(location: Location) {
        passLocation(GeoClue2LocationInformation(location, StateID.ON))
    }

    override fun close() {
        client?.disconnectSignals()
        client?.unref()
        this.client = null
    }

    init {
        try {
            val appId = AppConfig.getInstance().appId
            Simple.newWithThresholds(Str(appId), AccuracyLevel.EXACT, 0, 0, null,
                { self, _ , res: AsyncResult, _ ->
                    try {
                        val simple = getSimpleOrThrow(res)
                        val clientProxy = getClientProxyOrThrow(simple)
                        clientProxy.ref()
                        AppLog.d(this, "Client object: " + getClientID(clientProxy))
                        clientProxy.onNotify { updateStateAndLocation(simple.location) }
                        this.client = clientProxy
                        updateStateAndLocation(simple.location)
                    } catch (e: java.lang.Exception) {
                        passState(StateID.NO_SERVICE)
                        AppLog.e(this, "Failed to connect to GeoClue2 service")
                    }
                    self.unregister()
                }, null
            )
        } catch (e: java.lang.Exception) {
            passState(StateID.NO_SERVICE)
            AppLog.e(this, "Failed to initialize GeoClue2")
        }
    }

    companion object {
        private fun getClientProxyOrThrow(simple: Simple): ClientProxy {
            val result: ClientProxy? = simple.client

            if (result is ClientProxy) {
                return result
            }
            throw Exception("Failed to create ClientProxy")
        }

        private fun getSimpleOrThrow(res: AsyncResult): Simple {
            val result: Simple? = Simple.newWithThresholdsFinishSimple(res)
            if (result is Simple) {
                return result
            }
            throw Exception("Failed to create simple connection")
        }

        private fun getClientID(client: ClientProxy): String {
            // client.objectPath might be null inside flatpak
            val result: Str? = client.objectPath
            if (result is Str && result.isNotNull) {
                return result.toString()
            }
            return "unknown"
        }
    }
}
