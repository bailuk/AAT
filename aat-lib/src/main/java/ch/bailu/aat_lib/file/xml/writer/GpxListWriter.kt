package ch.bailu.aat_lib.file.xml.writer

import ch.bailu.aat_lib.file.xml.writer.GpxWriter.Companion.factory
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListIterator
import ch.bailu.foc.Foc
import java.io.Closeable
import java.io.IOException

class GpxListWriter(track: GpxList, file: Foc) : Closeable {
    private val iterator = GpxListIterator(track)
    private val writer = factory(file, track.getDelta().getType())


    init {
        writer.writeHeader(System.currentTimeMillis())
    }

    @Throws(IOException::class)
    override fun close() {
        flushOutput()
        writer.writeFooter()
        writer.close()
    }

    @Throws(IOException::class)
    fun flushOutput() {
        while (iterator.nextPoint()) {
            if (iterator.isFirstInSegment) {
                if (iterator.isFirstInTrack) {
                    writer.writeFirstSegment()
                } else {
                    writer.writeSegment()
                }
            }
            writer.writeTrackPoint(iterator.point)
        }
    }
}
