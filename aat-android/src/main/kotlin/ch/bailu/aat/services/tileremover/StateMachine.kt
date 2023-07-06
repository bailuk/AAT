package ch.bailu.aat.services.tileremover

import ch.bailu.aat.preferences.map.SolidTrimIndex
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.foc.Foc

class StateMachine(val appContext: AppContext) : State {

    @JvmField
    var list: TilesList = TilesList()

    @JvmField
    val summaries = SourceSummaries()

    private var state: State = StateUnscanned(this)

    @JvmField
    var baseDirectory: Foc = Foc.FOC_NULL

    @Synchronized
    fun set(s: State) {
        state = s
    }

    @Synchronized
    override fun scan() {
        state.scan()
    }

    @Synchronized
    override fun stop() {
        state.stop()
    }

    @Synchronized
    override fun reset() {
        state.reset()
    }

    @Synchronized
    override fun remove() {
        state.remove()
    }

    @Synchronized
    override fun rescan() {
        state.rescan()
    }

    @Synchronized
    override fun removeAll() {
        state.removeAll()
    }

    @Synchronized
    fun setFromClass(s: Class<*>) {
        if (s == StateRemoved::class.java) {
            set(StateRemoved(this))
        } else if (s == StateScanned::class.java) {
            set(StateScanned(this))
        } else if (s == StateUnscanned::class.java) {
            set(StateUnscanned(this))
        } else if (s == StateScannedPartial::class.java) {
            set(StateScannedPartial(this))
        } else if (s == StateScanForRemoval::class.java) {
            set(StateScanForRemoval(this))
        } else if (s == StateScan::class.java) {
            set(StateScan(this))
        } else if (s == StateRemoveAll::class.java) {
            set(StateRemoveAll(this))
        }
    }

    val info: SelectedTileDirectoryInfo
        get() {
            val index = SolidTrimIndex(appContext.storage).value
            val name = summaries[index].name
            var subDirectory = baseDirectory
            if (index > 0) subDirectory = baseDirectory.child(name)
            return SelectedTileDirectoryInfo(subDirectory, name, index)
        }
    private var stamp: Long = 0

    init {
        set(StateUnscanned(this))
    }

    fun broadcastLimited(message: String) {
        val stamp = System.currentTimeMillis()
        if (stamp - this.stamp > LIMIT) {
            this.stamp = stamp
            broadcast(message)
        }
    }

    fun broadcast(message: String) {
        appContext.broadcaster.broadcast(message)
    }

    companion object {
        private const val LIMIT: Long = 100
    }
}
