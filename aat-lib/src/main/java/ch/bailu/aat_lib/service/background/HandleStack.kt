package ch.bailu.aat_lib.service.background

import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

class HandleStack(private val limit: Int = DEFAULT_LIMIT) {
    private val queue: BlockingDeque<BackgroundTask> = LinkedBlockingDeque(limit)

    @Throws(InterruptedException::class)
    fun take(): BackgroundTask {
        return queue.takeFirst()
    }

    fun offer(handle: BackgroundTask) {
        while (queue.size >= limit) remove()
        insert(handle)
    }

    fun close(i: Int) {
        var i = i
        while (remove() != null);
        while (i > 0) {
            queue.offerFirst(BackgroundTask.NULL)
            i--
        }
    }

    private fun insert(handle: BackgroundTask) {
        handle.onInsert()
        queue.offerFirst(handle)
    }

    private fun remove(): BackgroundTask? {
        val handle = queue.pollLast()

        handle?.onRemove()
        return handle
    }

    companion object {
        private const val DEFAULT_LIMIT = 5000
    }
}
