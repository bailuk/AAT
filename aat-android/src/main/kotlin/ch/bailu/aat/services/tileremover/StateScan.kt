package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.foc.Foc

/**
 * TODO move to lib
 */
class StateScan(private val state: StateMachine) : State, Runnable {
    private var nextState: Class<*> = StateScanForRemoval::class.java

    init {
        state.baseDirectory = state.appContext.tileCacheDirectory.getValueAsFile()
        state.list = TilesList()
        Thread(this).start()
    }

    override fun scan() {
        nextState = StateScanForRemoval::class.java
    }

    override fun stop() {
        nextState = StateUnscanned::class.java
    }

    override fun reset() {
        nextState = StateUnscanned::class.java
    }

    override fun remove() {}
    override fun removeAll() {
        nextState = StateRemoveAll::class.java
    }

    override fun rescan() {}
    override fun run() {
        scanSourceContainer(state.baseDirectory)
        if (keepUp()) {
            state.broadcast(AppBroadcaster.TILE_REMOVER_SCAN)
        }
        state.setFromClass(nextState)
    }

    private fun keepUp(): Boolean {
        return nextState == StateScanForRemoval::class.java
    }

    private fun scanSourceContainer(sourceContainer: Foc) {
        try {
            state.summaries.rescan(sourceContainer)
            for (summaryIndex in 1 until state.summaries.size()) {
                val sourceName = state.summaries[summaryIndex].name
                if (sourceName.isNotEmpty()) {
                    val zoomContainer = sourceContainer.child(sourceName)
                    scanZoomContainer(zoomContainer, summaryIndex)
                } else {
                    break
                }
            }
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }

    private fun scanZoomContainer(zoomContainer: Foc, summaryIndex: Int) {
        object : TileScanner(zoomContainer) {
            override fun doSourceContainer(dir: Foc): Boolean {
                return keepUp()
            }

            override fun doZoomContainer(dir: Foc): Boolean {
                return keepUp()
            }

            override fun doXContainer(dir: Foc): Boolean {
                return keepUp()
            }

            override fun doYContainer(dir: Foc): Boolean {
                state.broadcastLimited(AppBroadcaster.TILE_REMOVER_SCAN)
                return keepUp()
            }

            override fun doFile(file: Foc) {
                val tile = TileFile(summaryIndex, zoom, x, file)
                state.list.add(tile)
                state.summaries.addFile(tile)
            }
        }.scanZoomContainer()
    }
}
