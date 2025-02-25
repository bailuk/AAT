package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.logger.AppLog.w
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import javax.annotation.Nonnull

abstract class Obj(private val id: String) : ObjBroadcastReceiver {
    private var accessTime = System.currentTimeMillis()
    private var lock = 0
    private var exception: Exception? = null

    @Nonnull
    override fun toString(): String {
        return id
    }

    fun getID(): String {
        return id
    }

    open fun getFile(): Foc {
        w(this, "Default implementation of getFile() called!")
        return FocName(id)
    }

    protected fun setException(e: Exception?) {
        exception = e
    }

    fun hasException(): Boolean {
        return exception != null
    }

    fun getException(): Exception? {
        return exception
    }

    fun isLocked(): Boolean {
        return lock > 0
    }

    open fun onInsert(appContext: AppContext) {}
    open fun onRemove(appContext: AppContext) {}

    @Synchronized
    fun lock() {
        lock++
        access()
    }

    @Synchronized
    fun free() {
        lock--
    }

    open fun isReadyAndLoaded(): Boolean {
        return true
    }

    open fun isLoaded(): Boolean {
        return isReadyAndLoaded() || hasException()
    }

    abstract fun getSize(): Long

    @Synchronized
    open fun access() {
        accessTime = System.currentTimeMillis()
    }

    @Synchronized
    fun getAccessTime(): Long {
        return accessTime
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    open class Factory {
        open fun factory(id: String, appContext: AppContext): Obj {
            return ObjNull
        }
    }

    companion object {
        const val MIN_SIZE: Int = 100
    }
}
