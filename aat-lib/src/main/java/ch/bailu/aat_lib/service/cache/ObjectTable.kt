package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.BroadcastData.getFile
import ch.bailu.aat_lib.util.MemSize

class ObjectTable {
    private var limit = MemSize.MB
    private var totalMemorySize: Long = 0


    private class Container(val obj: Obj) {
        var size: Long = obj.getSize()

        companion object {
            val NULL: Container = Container(ObjNull)
        }
    }

    private val hashMap = HashMap<String, Container>(INITIAL_CAPACITY)

    @Synchronized
    fun getHandle(key: String): Obj {
        var obj = getFromCache(key)

        if (obj == null) {
            obj = ObjNull
        }

        obj.lock()
        return obj
    }


    @Synchronized
    fun getHandle(id: String, factory: Obj.Factory, self: CacheService): Obj {
        val h = getHandle(id, factory, self.appContext)
        trim(self)
        return h
    }


    @Synchronized
    fun getHandle(id: String, factory: Obj.Factory, appContext: AppContext): Obj {
        var h = getFromCache(id)

        if (h == null) {
            h = factory.factory(id, appContext)

            putIntoCache(h)

            h.lock()
            h.onInsert(appContext)
        } else {
            h.lock()
        }
        return h
    }


    @Synchronized
    private fun putIntoCache(obj: Obj) {
        hashMap[obj.toString()] = Container(obj)
        totalMemorySize += obj.getSize()
    }


    @Synchronized
    private fun getFromCache(key: String): Obj? {
        val c = hashMap[key]
        if (c != null) return c.obj

        return null
    }


    @Synchronized
    private fun updateSize(id: String): Boolean {
        val c = hashMap[id]

        if (c != null) {
            val oSize = c.size
            val nSize = c.obj.getSize()

            totalMemorySize -= oSize
            c.size = nSize
            totalMemorySize += nSize

            return nSize > oSize
        }
        return false
    }


    fun onObjectChanged(self: CacheService, vararg objs: String) {
        if (updateSize(toID(*objs))) trim(self)
    }


    private fun toID(vararg objs: String): String {
        return getFile(objs)
    }


    @Synchronized
    fun limit(self: CacheService, l: Long) {
        limit = l
        trim(self)
    }


    private fun trim(self: CacheService) {
        while ((totalMemorySize > limit) && removeOldest(self));
    }


    private fun removeOldest(self: CacheService): Boolean {
        return removeFromTable(findOldest(), self)
    }


    fun close(self: CacheService) {
        for (current in hashMap.values) {
            current.obj.onRemove(self.appContext)
        }
        hashMap.clear()
    }


    @Synchronized
    private fun removeFromTable(id: String, self: CacheService): Boolean {
        val remove = hashMap[id]

        if (remove != null && !remove.obj.isLocked()) {
            self.broadcaster.delete(remove.obj)
            hashMap.remove(id)
            totalMemorySize -= remove.size
            remove.obj.onRemove(self.appContext)
            return true
        }
        return false
    }


    @Synchronized
    private fun findOldest(): String {
        var oldest = Container.NULL
        oldest.obj.access()

        for (current in hashMap.values) {
            if ((!current.obj.isLocked()) && (current.obj.getAccessTime() < oldest.obj.getAccessTime())) {
                oldest = current
            }
        }

        return oldest.obj.getID()
    }


    @Synchronized
    fun appendStatusText(builder: StringBuilder) {
        builder.append("<p>Runtime:")

        builder.append("<br>Maximum memory: ")
        MemSize.describe(builder, Runtime.getRuntime().maxMemory())
        builder.append("<br>Total memory: ")
        MemSize.describe(builder, Runtime.getRuntime().totalMemory())
        builder.append("<br>Free memory: ")
        MemSize.describe(builder, Runtime.getRuntime().freeMemory())
        builder.append("<br>Used memory: ")
        MemSize.describe(
            builder,
            Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        )
        builder.append("</p>")

        builder.append("<p> FileCache:")
        builder.append("<br>Used: ")
        MemSize.describe(builder, totalMemorySize)
        builder.append("<br>Limit: ")
        MemSize.describe(builder, limit)
        builder.append("<br>Free: ")
        MemSize.describe(builder, limit - totalMemorySize)
        builder.append("</p>")


        var locked = 0
        var free = 0

        for (current in hashMap.values) {
            if (current.obj.isLocked()) locked++
            else free++
        }

        builder.append("<br>LOCKED cache entries: ")
        builder.append(locked)

        builder.append("<br>FREE cache entries: ")
        builder.append(free)

        builder.append("<br>TOTAL cache entries: ")
        builder.append(hashMap.size)
        builder.append("</p>")
    }

    companion object {
        private const val INITIAL_CAPACITY = 1000
    }
}
