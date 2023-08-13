package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.app.AppContext

abstract class BackgroundTask : ThreadControl {

    private var processing = true

    override fun canContinue(): Boolean {
        return processing
    }

    abstract fun bgOnProcess(appContext: AppContext): Long

    @Synchronized
    fun stopProcessing() {
        processing = false
    }

    val threadControl: ThreadControl
        get() = this

    open fun onInsert() {}
    open fun onRemove() {}

    companion object {
        @JvmField
        val NULL: BackgroundTask = object : BackgroundTask() {
            override fun bgOnProcess(appContext: AppContext): Long {
                return 0
            }
        }
        @JvmField
        val STOP: BackgroundTask = object : BackgroundTask() {
            override fun bgOnProcess(appContext: AppContext): Long {
                return 0
            }

            override fun canContinue(): Boolean {
                return false
            }
        }
    }
}
