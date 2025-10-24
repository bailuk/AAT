package ch.bailu.aat_lib.api

import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.foc.Foc

abstract class Api(private val overlay: SolidOverlayInterface) {
    val apiName: String
        get() = overlay.getLabel()

    val resultFile: Foc
        get() = overlay.getValueAsFile()

    fun isTaskRunning(scontext: ServicesInterface): Boolean {
        var running = false
        scontext.insideContext {
            val background = scontext.getBackgroundService()
            running = background.findTask(resultFile) != null
        }
        return running
    }

    fun stopTask(scontext: ServicesInterface) {
        scontext.insideContext {
            val background = scontext.getBackgroundService()
            val task: FileTask? = background.findTask(resultFile)
            task?.stopProcessing()
        }
    }

}