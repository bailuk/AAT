package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.app.AppContext

open class WorkerThread : ProcessThread {
    private val appContext: AppContext


    constructor(name: String, appContext: AppContext, limit: Int) : super(name, limit) {
        this.appContext = appContext
    }


    constructor(name: String, appContext: AppContext, q: HandleStack) : super(name, q) {
        this.appContext = appContext
    }

    override fun bgOnHandleProcessed(handle: BackgroundTask, size: Long) {}
    override fun bgProcessHandle(handle: BackgroundTask) {
        if (handle.canContinue()) {
            appContext.services.insideContext {
                val size = handle.bgOnProcess(appContext)
                bgOnHandleProcessed(handle, size)
            }
        }
    }
}
