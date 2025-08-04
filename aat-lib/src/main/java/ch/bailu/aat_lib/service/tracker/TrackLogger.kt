package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.file.xml.writer.GpxListWriter
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes.Companion.factoryTrack
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidAutopause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidPostprocessedAutopause
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import java.io.IOException

class TrackLogger(val sdirectory: SolidDataDirectory, private val presetIndex: Int) : Logger() {
    private var requestSegment = true
    private val track: GpxList = createTrack(sdirectory.getStorage(), presetIndex)
    private val logFile: Foc
    private val writer: GpxListWriter

    init {
        TrackCrashRestorer(sdirectory, presetIndex)
        logFile = AppDirectory.getLogFile(sdirectory)
        writer = GpxListWriter(track, logFile)
        setVisibleTrackSegment(track.getDelta())
    }

    override fun getFile(): Foc {
        return logFile
    }

    override fun getLoaded(): Boolean {
        return true
    }

    override fun getGpxList(): GpxList {
        return track
    }

    override fun logPause() {
        if (track.pointList.size() > 0) {
            requestSegment = true
        }
    }

    @Throws(IOException::class)
    override fun log(tp: GpxPointInterface, attr: GpxAttributes) {
        if (requestSegment) {
            requestSegment = false
            track.appendToNewSegment(GpxPoint(tp), attr)
        } else {
            track.appendToCurrentSegment(GpxPoint(tp), attr)
        }
        setVisibleTrackPoint(track.pointList.last as GpxPointNode?)
        writer.flushOutput()
    }

    override fun close() {
        try {
            writer.close()
            if (track.pointList.size() > MIN_TRACK_POINTS) {
                logFile.move(generateTargetFile(sdirectory, presetIndex))
            } else {
                logFile.remove()
            }
        } catch (e: IOException) {
            AppLog.e(this, e)
        }
    }

    companion object {
        const val MIN_TRACK_POINTS = 5
        private fun createTrack(s: StorageInterface, presetIndex: Int): GpxList {
            val spause: SolidAutopause = SolidPostprocessedAutopause(s, presetIndex)
            return GpxList(GpxType.TRACK, factoryTrack(spause))
        }

        @JvmStatic
        @Throws(IOException::class)
        fun generateTargetFile(sdirectory: SolidDataDirectory, preset: Int): Foc {
            return AppDirectory.generateUniqueFilePath(
                AppDirectory.getTrackListDirectory(sdirectory, preset),
                AppDirectory.generateDatePrefix(),
                AppDirectory.GPX_EXTENSION
            )
        }
    }
}
