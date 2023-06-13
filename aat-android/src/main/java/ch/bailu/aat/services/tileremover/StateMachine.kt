package ch.bailu.aat.services.tileremover

import android.content.Context
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.SolidTrimIndex
import ch.bailu.aat.services.ServiceContext
import ch.bailu.foc.Foc

class StateMachine(sc: ServiceContext) : State {
    private var state: State = StateUnscanned(this)

    @JvmField
    var list: TilesList? = null
    @JvmField
    val summaries: SourceSummaries
    @JvmField
    var baseDirectory: Foc? = null
    @JvmField
    val context: Context
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
            val index = SolidTrimIndex(Storage(context)).value
            val name = summaries[index].name
            var subDirectory = baseDirectory
            if (index > 0) subDirectory = baseDirectory!!.child(name)
            return SelectedTileDirectoryInfo(baseDirectory, subDirectory, name, index)
        }
    private var stamp: Long = 0

    init {
        summaries = SourceSummaries(sc.context)
        context = sc.context
        set(StateUnscanned(this))
    }

    fun broadcastLimited(msg: String) {
        val stamp = System.currentTimeMillis()
        if (stamp - this.stamp > LIMIT) {
            this.stamp = stamp
            broadcast(msg)
        }
    }

    fun broadcast(msg: String) {
        AndroidBroadcaster.broadcast(context, msg)
    }

    companion object {
        private const val LIMIT: Long = 100
    }
}
