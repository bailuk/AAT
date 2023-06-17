package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.foc.Foc

class StateRemoveAll(private val state: StateMachine) : State, Runnable {
    private var nextState: Class<*> = StateRemoved::class.java

    init {
        Thread(this).start()
    }

    override fun stop() {
        nextState = StateUnscanned::class.java
    }

    override fun reset() {
        nextState = StateUnscanned::class.java
    }

    override fun scan() {}
    override fun remove() {}
    override fun rescan() {}
    override fun removeAll() {}
    override fun run() {
        val info = state.info
        val scanner: TileScanner = object : TileScanner(info.directory) {
            var sourceIndex = 0
            override fun doSourceContainer(dir: Foc): Boolean {
                return keepUp()
            }

            override fun doZoomContainer(dir: Foc): Boolean {
                sourceIndex = state.summaries.findIndex(source)
                return keepUp()
            }

            override fun doXContainer(dir: Foc): Boolean {
                return keepUp()
            }

            override fun doYContainer(dir: Foc): Boolean {
                return keepUp()
            }

            override fun doFile(file: Foc) {
                delete(file, TileFile(sourceIndex, zoom, x, y, file))
            }
        }
        if (info.index == 0) {
            scanner.scanSourceContainer()
        } else {
            scanner.scanZoomContainer()
        }
        if (keepUp()) {
            info.directory.rmdirs()
            //MemSize.deleteEmptiyDirectoriesRecursive(info.directory);
            broadcast()
        }
        state.setFromClass(nextState)
    }

    private fun delete(f: Foc, t: TileFile): Boolean {
        if (f.rm()) {
            state.summaries.addFileRemoved(t)
            state.broadcastLimited(AppBroadcaster.TILE_REMOVER_REMOVE)
            return true
        }
        return false
    }

    private fun keepUp(): Boolean {
        return nextState == StateRemoved::class.java
    }

    private fun broadcast() {
        state.appContext.broadcaster.broadcast(AppBroadcaster.TILE_REMOVER_REMOVE)
    }
}
