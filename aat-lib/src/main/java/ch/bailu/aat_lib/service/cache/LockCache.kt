package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.logger.AppLog
import java.io.Closeable

class LockCache<E : Obj?>(capacity: Int) : Closeable {
    private var objects: Array<E?> = arrayOfNulls<Obj>(capacity) as Array<E?>
    private var access = LongArray(capacity)
    private var size = 0

    fun size(): Int {
        return size
    }

    operator fun get(i: Int): E? {
        return objects[i]
    }

    fun use(i: Int): E? {
        objects[i]?.access()
        access[i] = System.currentTimeMillis()
        return objects[i]
    }

    fun add(handle: E) {
        val i: Int

        if (size < objects.size) {
            i = size
            size++
        } else {
            i = indexOfOldest()
            objects[i]?.free()
        }
        objects[i] = handle

        use(i)
    }

    private fun indexOfOldest(): Int {
        var x = 0

        for (i in 1 until size) {
            if (access[i] < access[x]) {
                x = i
            }
        }
        return x
    }

    override fun close() {
        reset()
    }

    fun reset() {
        for (i in 0 until size) {
            objects[i]?.free()
        }
        size = 0
    }

    fun ensureCapacity(capacity: Int) {
        if (capacity > objects.size) {
            AppLog.d(this, "Grow capacity from ${objects.size} to $capacity")
            resizeCache(capacity)
        }
    }

    private fun resizeCache(capacity: Int) {
        val newObjects = arrayOfNulls<Obj>(capacity) as Array<E?>
        val newAccess = LongArray(capacity)

        val l = newObjects.size.coerceAtMost(objects.size)

        var i = 0
        while (i < l) {
            newObjects[i] = objects[i]
            newAccess[i] = access[i]
            i++
        }

        while (i < size) {
            objects[i]?.free()
            i++
        }

        objects = newObjects
        access = newAccess
        size = size.coerceAtMost(objects.size)
    }
}
