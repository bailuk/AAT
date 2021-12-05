package ch.bailu.aat_gtk.service.location

import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.location.*
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory

class ThreadedMockLocation(
    locationService: LocationServiceInterface,
    item: LocationStackItem,
    storage: StorageInterface,
    foc: FocFactory
) :
    LocationStackChainedItem(item) {
    private var serviceState = StateID.NOSERVICE
    private val file: Foc = SolidGtkDataDirectory(storage, foc).valueAsFile.child("test.gpx")
    private val innerThread = InnerThread()
    private val lock = locationService

    init {
        passState(StateID.WAIT)
        innerThread.start()
    }

    private inner class InnerThread : Thread() {
        var running = true

        override fun run() {
            val list = GpxListReader(file, AutoPause.NULL).gpxList
            var node = list.pointList.first
            passState(StateID.ON)
            while (running && node is GpxPointNode) {
                passLocation(MockLocationInformation(file, serviceState, node))
                node = node.next
                if (running && node is GpxPointNode) {
                    try {
                        sleep(node.timeDelta)
                    } catch (e: InterruptedException) {
                        AppLog.e(this, e)
                    }
                }
            }
            passState(StateID.OFF)
        }
    }

    override fun close() {
        innerThread.running = false
        innerThread.interrupt()
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