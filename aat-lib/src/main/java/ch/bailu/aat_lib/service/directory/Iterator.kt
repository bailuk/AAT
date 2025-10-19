package ch.bailu.aat_lib.service.directory

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import java.io.Closeable

abstract class Iterator : Closeable {
    abstract fun getID(): Long

    open fun getInfoID(): Int {
        return InfoID.FILE_VIEW
    }

    open fun moveToPrevious(): Boolean {
        return false
    }

    open fun moveToNext(): Boolean {
        return false
    }

    open fun moveToPosition(pos: Int): Boolean {
        return false
    }

    open fun getCount(): Int {
        return 0
    }

    open fun getPosition(): Int {
        return 0
    }

    abstract fun getInfo(): GpxInformation

    open fun query() {}
    override fun close() {}

    open fun setOnCursorChangedListener(listener: ()->Unit) {}

    companion object {
        val NULL: Iterator = object : Iterator() {
            override fun getID(): Long {
                return 0L
            }

            override fun getInfo(): GpxInformation {
                return GpxInformation.NULL
            }
        }
    }
}
