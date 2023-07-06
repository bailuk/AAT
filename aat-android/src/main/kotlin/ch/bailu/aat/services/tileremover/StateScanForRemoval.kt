package ch.bailu.aat.services.tileremover

import ch.bailu.aat.preferences.map.SolidTrimDate
import ch.bailu.aat.preferences.map.SolidTrimIndex
import ch.bailu.aat.preferences.map.SolidTrimMode
import ch.bailu.aat.preferences.map.SolidTrimSize
import ch.bailu.aat_lib.dispatcher.AppBroadcaster

class StateScanForRemoval(private val state: StateMachine) : State, Runnable {
    private var nextState: Class<*> = StateScanned::class.java
    private val trimMode: Int = SolidTrimMode(state.appContext.storage).index
    private val trimSummaryIndex: Int = SolidTrimIndex(state.appContext.storage).value
    private val trimSize: Long = SolidTrimSize(state.appContext.storage).getValue()
    private val trimAge: Long = System.currentTimeMillis() - SolidTrimDate(state.appContext.storage).getValue()

    init {
        state.summaries.resetToRemove()
        state.list.resetToRemove()
        Thread(this).start()
    }

    override fun scan() {
        nextState = StateScanned::class.java
    }

    override fun stop() {
        nextState = StateScannedPartial::class.java
    }

    override fun reset() {
        nextState = StateUnscanned::class.java
    }

    override fun remove() {}
    override fun removeAll() {
        nextState = StateRemoveAll::class.java
    }

    override fun rescan() {
        nextState = StateScanForRemoval::class.java
    }

    override fun run() {
        val iterator = state.list.iterator()
        while (iterator.hasNext() && keepUp()) {
            val file = iterator.next()
            if (passFilter(file)) {
                if (passDirectory(file)) addFile(file)
                state.broadcastLimited(AppBroadcaster.TILE_REMOVER_SCAN)
            } else {
                break
            }
        }
        state.setFromClass(nextState)
    }

    private fun addFile(file: TileFile) {
        state.summaries.addFileToRemove(file)
        state.list.addToRemove(file)
    }

    private fun passFilter(file: TileFile): Boolean {
        if (trimMode == SolidTrimMode.MODE_TO_SIZE) {
            return passSize()
        } else if (trimMode == SolidTrimMode.MODE_TO_AGE) {
            return passAge(file)
        } else if (trimMode == SolidTrimMode.MODE_TO_SIZE_AND_AGE) {
            return passSize() || passAge(file)
        } else if (trimMode == SolidTrimMode.MODE_TO_SIZE_OR_AGE) {
            return passSize() && passAge(file)
        }
        return false
    }

    private fun passDirectory(file: TileFile): Boolean {
        return trimSummaryIndex == 0 || file.source == trimSummaryIndex
    }

    private fun passSize(): Boolean {
        return state.summaries[trimSummaryIndex].sizeNew > trimSize
    }

    private fun passAge(file: TileFile): Boolean {
        return file.lastModified() < trimAge
    }

    private fun keepUp(): Boolean {
        return nextState == StateScanned::class.java
    }
}
