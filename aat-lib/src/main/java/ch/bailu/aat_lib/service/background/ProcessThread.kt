package ch.bailu.aat_lib.service.background

import java.io.Closeable

abstract class ProcessThread(name: String, private val queue: HandleStack) : Thread(name), Closeable, ThreadControl {

    private var continueThread = true
    private var current = BackgroundTask.NULL

    init {
        start()
    }

    constructor(name: String, limit: Int) : this(name, HandleStack(limit))

    override fun run() {
        while (canContinue()) {
            try {
                current = queue.take()
                bgProcessHandle(current)
                current.onRemove()
                current = BackgroundTask.NULL
            } catch (e: InterruptedException) {
                continueThread = false
                e.printStackTrace()
            }
        }
    }

    abstract fun bgOnHandleProcessed(handle: BackgroundTask, size: Long)
    abstract fun bgProcessHandle(handle: BackgroundTask)

    fun process(handle: BackgroundTask) {
        if (canContinue()) queue.offer(handle)
    }

    override fun close() {
        continueThread = false
        current.stopProcessing()
        queue.offer(BackgroundTask.STOP)
    }

    override fun canContinue(): Boolean {
        return continueThread
    }
}
