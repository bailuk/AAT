package ch.bailu.aat_lib.api.brouter

import ch.bailu.aat_lib.api.Api
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListIterator
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayInterface
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.aat_lib.util.Limit

/**
 * curl 'https://brouter.de/brouter?lonlats=2.179306,48.994597|2.054366,48.851715&profile=trekking&alternativeidx=0&format=gpx' -o brouter.gpx
 *
 */
class BrouterApi(overlay: SolidOverlayInterface) : Api(overlay) {
    fun startTask(appContext: AppContext, gpxList: GpxList) {
        if (validateLimit(gpxList)) {
            appContext.services.insideContext {
                val background = appContext.services.getBackgroundService()

                val url =
                    "https://brouter.de/brouter?lonlats=${getCoordinateParameter(gpxList)}&profile=trekking&alternativeidx=0&format=gpx"
                val task = DownloadTask(url, resultFile, appContext.downloadConfig)
                background.process(task)
            }
        }
    }

    private fun validateLimit(gpxList: GpxList): Boolean {
        return Limit.isInRange(gpxList.pointList.size(), 2, 15)
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
