package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import java.util.concurrent.Callable
import java.util.concurrent.CompletionService
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.Executors

// TODO move to lib
class StateRemove(private val state: StateMachine) : State, Runnable {
    private var nextState: Class<*> = StateRemoved::class.java

    init {
        Thread(this).start()
    }

    override fun scan() {}
    override fun stop() {
        nextState = StateScanned::class.java
    }

    override fun reset() {
        nextState = StateUnscanned::class.java
    }

    override fun remove() {}
    override fun removeAll() {}
    override fun rescan() {}
    private inner class TaskDelete(val tileFile: TileFile) : Callable<TileFile?> {
        override fun call(): TileFile? {
            val f = state.summaries.toFile(state.baseDirectory, tileFile)
            return if (f.rm()) {
                tileFile
            } else null
        }
    }

    override fun run() {
        val iterator = state.list.iteratorToRemove()
        val executor = Executors.newFixedThreadPool(10)
        val completion: CompletionService<TileFile> = ExecutorCompletionService(executor)
        var tasks = 0
        while (iterator.hasNext() && keepUp()) {
            val t = iterator.next()
            completion.submit(TaskDelete(t))
            tasks++
        }
        while (tasks > 0 && keepUp()) {
            tasks--
            try {
                val future = completion.take()
                val t = future.get()
                if (t != null) {
                    state.summaries.addFileRemoved(t)
                }
                state.broadcastLimited(AppBroadcaster.TILE_REMOVER_REMOVE)
            } catch (e: InterruptedException) {
                AppLog.w(this, e)
                break
            } catch (e: ExecutionException) {
                AppLog.w(this, e)
            }
        }
        executor.shutdownNow()
        state.list.resetToRemove()
        if (keepUp()) {
            state.broadcast(AppBroadcaster.TILE_REMOVER_REMOVE)
            state.baseDirectory.rmdirs()
        }
        state.setFromClass(nextState)
    }

    private fun keepUp(): Boolean {
        return nextState == StateRemoved::class.java
    }
}
