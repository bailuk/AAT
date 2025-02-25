package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.StateID
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import java.io.Closeable
import java.io.IOException

abstract class Logger : GpxInformation(), Closeable {
    private var state = StateID.OFF

    open fun logPause() {}

    @Throws(IOException::class)
    abstract fun log(tp: GpxPointInterface, attr: GpxAttributes)

    override fun close() {}

    fun setState(s: Int) {
        state = s
    }

    override fun getState(): Int {
        return state
    }

    companion object {
        val NULL_LOGGER: Logger = object : Logger() {
            override fun log(tp: GpxPointInterface, attr: GpxAttributes) {}
        }
    }
}
