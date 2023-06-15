package ch.bailu.aat_gtk.service.location

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.gpx.linked_list.Node
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.service.location.*
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory


/**
 * TODO move to lib and merge with MockLocation
 */
class ThreadedMockLocation(
    locationService: LocationServiceInterface,
    item: LocationStackItem,
    storage: StorageInterface,
    private val foc: FocFactory
) :

    LocationStackChainedItem(item) {
    private var serviceState = StateID.NOSERVICE

    private val innerThread = InnerThread()

    private val lock = locationService
    private val smock = SolidMockLocationFile(storage)
    private var reload = true
    private var waiting = false
    private var running = true

    private var file = foc.toFoc(smock.valueAsString)

    init {
        passState(StateID.WAIT)
        innerThread.start()
        storage.register { _, key ->
            if (smock.hasKey(key)) {
                file = foc.toFoc(smock.valueAsString)
                reload = true
                synchronized(innerThread) {
                    (innerThread as Object).notifyAll()
                }
            }
        }
    }

    private inner class InnerThread : Thread() {

        override fun run() {
            var node: Node? = null

            while (running) {
                if (reload) {
                    AppLog.d(this, "reload")
                    node = loadTrack(file)
                    waiting = if (node is GpxPointNode) {
                        passState(StateID.ON)
                        false
                    } else {
                        true
                    }
                    reload = false
                }

                if (node is GpxPointNode) {
                    passLocation(MockLocationInformation(file, serviceState, node))
                    node = node.next
                    if (node is GpxPointNode) {
                        try {
                            sleep(node.timeDelta)
                        } catch (e: InterruptedException) {
                            waiting = true
                            AppLog.e(this, e)
                        }
                    } else {
                        waiting = true
                    }
                } else {
                    waiting = true
                }

                if (waiting) {
                    passState(StateID.OFF)
                    reload = true
                    AppLog.d(this, "wait")
                    synchronized(this) {
                        (this as Object).wait()
                    }
                }
            }

        }

        private fun loadTrack(foc: Foc) : Node {
            val list = GpxListReader(foc , AutoPause.NULL).gpxList
            return list.pointList.first
        }
    }

    override fun close() {
        running = false

        synchronized(innerThread) {
            (innerThread as Object).notifyAll()
        }
    }

    override fun passLocation(location: LocationInformation) {
        synchronized(lock) { super.passLocation(location) }
    }

    override fun passState(s: Int) {
        synchronized(lock) {
            if (serviceState != s) {
                serviceState = s
                super.passState(s)
            }
        }
    }
}
