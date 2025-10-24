package ch.bailu.aat_lib.api.brouter

import ch.bailu.aat_lib.api.Api
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListIterator
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName

/**
 * curl 'https://brouter.de/brouter?lonlats=2.179306,48.994597|2.054366,48.851715&profile=trekking&alternativeidx=0&format=gpx' -o brouter.gpx
 *
 */
class BrouterApi : Api() {
    override val apiName: String = "Brouter"
    override var resultFile: Foc = FocName(apiName)
        private set

    fun startTask(appContext: AppContext, gpxList: GpxList) {
        if (validateLimit(gpxList)) {
            appContext.services.insideContext {
                val background = appContext.services.getBackgroundService()
                resultFile = getTarget(appContext)

                val url =
                    "https://brouter.de/brouter?lonlats=${getCoordinateParameter(gpxList)}&profile=trekking&alternativeidx=0&format=gpx"
                val task = DownloadTask(url, resultFile, appContext.downloadConfig)
                background.process(task)
            }
        }
    }

    private fun validateLimit(gpxList: GpxList): Boolean {
        return gpxList.pointList.size() > 0 && gpxList.pointList.size() < 10
    }

    private fun getTarget(appContext: AppContext): Foc {
        val dir = AppDirectory.getDataDirectory(
            appContext.dataDirectory,
            AppDirectory.DIR_QUERY)
        dir.mkdirs()
        return dir.child("brouter.gpx")
    }

    private fun getCoordinateParameter(gpxList: GpxList): String {
        val iterator = GpxListIterator(gpxList)
        val builder = StringBuilder()
        var del = ""
        while (iterator.nextPoint()) {
            builder.append("$del${iterator.point.getLongitude()},${iterator.point.getLatitude()}")
            del = "|"
        }
        return builder.toString()
    }
}
