package ch.bailu.aat_gtk.service.location.directory

import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.location.LocationStackChainedItem
import ch.bailu.aat_lib.service.location.LocationStackItem
import de.taimos.gpsd4java.api.IObjectListener
import de.taimos.gpsd4java.backend.GPSdEndpoint
import de.taimos.gpsd4java.types.*
import de.taimos.gpsd4java.types.subframes.SUBFRAMEObject
import java.lang.Exception

class GPSdLocationProvider(item: LocationStackItem?): LocationStackChainedItem(item), IObjectListener {
    private val gps = GPSdEndpoint("localhost", 2947)

    init {
        try {
            gps.addListener(this)
            gps.start()
            gps.watch(true, true)
            AppLog.d(this, "Watch mode enabled")
            passState(StateID.ON)

        } catch (e: Exception) {
            passState(StateID.NOACCESS)
            AppLog.e(this, e)
        }
    }

    override fun handleTPV(tpv: TPVObject?) {
        if (tpv is TPVObject) {
            AppLog.d(this, tpv.toString())
            passLocation(TPVLocationInformation(tpv))
        }

    }

    override fun handleSKY(sky: SKYObject?) {
        if (sky is SKYObject) {
            AppLog.d(this, sky.toString())
        }
    }

    override fun handleATT(att: ATTObject?) {
        if (att is ATTObject) {
            AppLog.d(this, att.toString())
        }
    }

    override fun handleSUBFRAME(subframe: SUBFRAMEObject?) {

    }

    override fun handleDevices(devices: DevicesObject?) {
        if (devices is DevicesObject) {
            AppLog.d(this, devices.toString())
        }
    }

    override fun handleDevice(device: DeviceObject?) {
        if (device is DeviceObject) {
            AppLog.d(this, device.toString())
        }
    }

    override fun close() {
        try {
            gps.removeListener(this)
            gps.stop()
            AppLog.d(this, "stopped")
            passState(StateID.OFF)
        } catch (e: Exception) {
            passState(StateID.NOACCESS)
            AppLog.e(this, e)
        }
    }
}
