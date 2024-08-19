package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.attributes.CadenceSpeedAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.sensor.SensorState

class CurrentSpeedDescription(storage: StorageInterface) : SpeedDescription(storage) {
    private var currentIID = -1
    private var label: String = ""

    init {
        setLabel()
    }

    private fun setLabel() {
        label = Res.str().speed()
        if (currentIID == InfoID.SPEED_SENSOR) {
            label += " S"
        } else if (currentIID == InfoID.LOCATION) {
            label += " GPS"
        }
    }

    override fun getLabel(): String {
        return label
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (configure(iid, info.getAttributes())) {
            if (!setSpeedFromLastPoint(info)) {
                setCache(info.getSpeed())
            }
        }
    }

    private fun configure(iid: Int, attr: GpxAttributes): Boolean {
        if (currentIID != iid) {
            if (changeSource(iid, attr)) {
                setLabel()
            }
        }
        return currentIID == iid
    }

    private fun changeSource(iid: Int, attr: GpxAttributes): Boolean {
        val haveSensor = SensorState.isConnected(InfoID.SPEED_SENSOR)
        return if (currentIID == InfoID.SPEED_SENSOR && haveSensor) {
            false
        } else if (iid == InfoID.SPEED_SENSOR) {
            val isSensorReady =
                haveSensor && attr.getAsBoolean(CadenceSpeedAttributes.KEY_INDEX_CONTACT)
            if (isSensorReady) {
                currentIID = iid
                true
            } else {
                false
            }
        } else {
            currentIID = iid
            true
        }
    }

    private fun setSpeedFromLastPoint(info: GpxInformation): Boolean {
        val track = info.getGpxList()
        if (track != null) {
            if (track.pointList.size() > 0) {
                val delta = info.getGpxList().pointList.last
                if (delta is GpxDeltaInterface) {
                    setCache(delta.getSpeed())
                    return true
                }
            }
        }
        return false
    }
}
