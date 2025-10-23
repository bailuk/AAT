package ch.bailu.aat_lib.api.cm

import ch.bailu.aat_lib.api.Api
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName

class CmApi : Api() {
    override val apiName: String = "CM"
    private val url = "https://api-gw.criticalmaps.net/locations"
    override var resultFile: Foc = FocName(apiName)
        private set

    fun startTask(appContext: AppContext) {
        appContext.services.insideContext {
            val background = appContext.services.getBackgroundService()
            resultFile = getTarget(appContext)
            val task = DownloadTask(url, resultFile, appContext.downloadConfig)
            background.process(task)
        }
    }

    private fun getTarget(appContext: AppContext): Foc {
        val dir = AppDirectory.getDataDirectory(
            appContext.dataDirectory,
            AppDirectory.DIR_QUERY)
        dir.mkdirs()
        return dir.child("cm.json")
    }
}
