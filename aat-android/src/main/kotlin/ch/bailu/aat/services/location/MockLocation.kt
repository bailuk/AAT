package ch.bailu.aat.services.location

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.service.location.LocationStackChainedItem
import ch.bailu.aat_lib.service.location.LocationStackItem
import ch.bailu.aat_lib.service.location.MockLocationInformation
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroid

/**
 * TODO move to lib and merge with ThreadedMockLocation
 */
class MockLocation(context: Context, next: LocationStackItem) : LocationStackChainedItem(next), Runnable {
    private var list: GpxList = GpxList(GpxType.TRACK, GpxListAttributes.NULL)
    private var node: GpxPointNode? = null
    private var state = StateID.NO_SERVICE
    private var interval = INTERVAL
    private val file: Foc = FocAndroid.factory(context, SolidMockLocationFile(Storage(context)).getValueAsString())

    private val timer: AndroidTimer = AndroidTimer()

    companion object {
        private const val INTERVAL = 1000L
    }

    init {
        list = GpxListReader(file, AutoPause.NULL).gpxList
        timer.kick(INTERVAL, this)
        passState(StateID.WAIT)
    }

    override fun close() {
        timer.cancel()
    }

    override fun run() {
        if (sendLocation()) {
            kickTimer()
        } else {
            node = list.pointList.first as GpxPointNode
            if (sendLocation()) {
                passState(StateID.ON)
                kickTimer()
            } else {
                passState(StateID.OFF)
            }
        }
    }

    private fun sendLocation(): Boolean {
        val currentNode = node
        if (currentNode != null) {
            passLocation(MockLocationInformation(file, state, currentNode))

            val nextNode = currentNode.next
            if (nextNode is GpxPointNode) {
                interval = nextNode.getTimeDelta()
                node = nextNode
            } else {
                node = null
            }
            return true
        }
        return false
    }

    private fun kickTimer() {
        if (interval <= 0 || interval > 10 * INTERVAL) {
            timer.kick(INTERVAL, this)
        } else {
            timer.kick(interval, this)
        }
    }

    override fun passState(state: Int) {
        if (this.state != state) {
            this.state = state
            super.passState(state)
        }
    }
}
