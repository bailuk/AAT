package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.file.xml.parser.gpx.GpxListReaderXml
import ch.bailu.aat_lib.file.xml.writer.GpxListWriter
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.resources.Res.str
import ch.bailu.aat_lib.service.tracker.TrackLogger.Companion.generateTargetFile
import ch.bailu.aat_lib.util.fs.AppDirectory.getLogFile
import ch.bailu.foc.Foc
import java.io.IOException

class TrackCrashRestorer(sdirectory: SolidDataDirectory, presetIndex: Int) {
    init {
        val source = getLogFile(sdirectory)
        if (source.exists()) {
            val track = readFile(source)
            if (track.pointList.size() > TrackLogger.MIN_TRACK_POINTS) {
                AppLog.i(this, str().tracker_restore())
                restoreFile(sdirectory, track, presetIndex)
            }
            source.rm()
        }
    }

    @Throws(IOException::class)
    private fun restoreFile(sdirectory: SolidDataDirectory, track: GpxList, presetIndex: Int) {
        val target = generateTargetFile(sdirectory, presetIndex)
        val writer = GpxListWriter(track, target)
        writer.close()
    }

    private fun readFile(remainingLogFile: Foc): GpxList {
        val reader = GpxListReaderXml(remainingLogFile, AutoPause.NULL)
        return reader.gpxList
    }
}
