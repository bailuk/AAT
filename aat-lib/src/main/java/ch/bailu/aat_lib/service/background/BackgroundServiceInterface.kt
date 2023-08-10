package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.util.WithStatusText
import ch.bailu.foc.Foc

interface BackgroundServiceInterface : WithStatusText {
    fun close()
    fun process(backgroundTask: BackgroundTask)
    fun findTask(file: Foc): FileTask?
}
