package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class LockCacheTest {

    @Test
    fun test() {
        val cache = LockCache<ObjTile>(5)

        assertEquals(0, cache.size())

        cache.add(ObjTile.NULL)
        assertEquals(1, cache.size())

        cache.add(ObjTile.NULL)
        assertEquals(2, cache.size())

        cache.add(ObjTile.NULL)
        assertEquals(3, cache.size())

        cache.add(ObjTile.NULL)
        assertEquals(4, cache.size())

        cache.add(ObjTile.NULL)
        assertEquals(5, cache.size())

        cache.add(ObjTile.NULL)
        assertEquals(5, cache.size())

        cache.ensureCapacity(6)
        assertEquals(5, cache.size())

        cache.add(ObjTile.NULL)
        assertEquals(6, cache.size())

        assertTrue(cache[0] is ObjTile)
        assertTrue(cache[5] is ObjTile)
    }

    @Test
    fun testAccess() {
        val cache = LockCache<Obj>(2)

        assertEquals(0, cache.size())

        val obj1 = object : Obj("1") {
            override fun onDownloaded(id: String?, url: String?, appContext: AppContext?) {}
            override fun onChanged(id: String?, appContext: AppContext?) {}
            override fun getSize(): Long {
                return 10
            }

        }


        val obj2 = object : Obj("2") {
            override fun onDownloaded(id: String?, url: String?, appContext: AppContext?) {}
            override fun onChanged(id: String?, appContext: AppContext?) {}
            override fun getSize(): Long {
                return 20
            }

        }

        val obj3 = object : Obj("3") {
            override fun onDownloaded(id: String?, url: String?, appContext: AppContext?) {}
            override fun onChanged(id: String?, appContext: AppContext?) {}
            override fun getSize(): Long {
                return 30
            }
        }

        cache.add(obj1)
        cache.add(obj2)
        cache.add(obj3)

        assertEquals("3", cache[0]!!.id)
        assertEquals("2", cache[1]!!.id)

        assertEquals(2, cache.size())

        cache.use(1)
        cache.add(obj1)

        assertEquals("1", cache[0]!!.id)
        assertEquals("2", cache[1]!!.id)

        cache.ensureCapacity(4)
        assertEquals(2, cache.size())

        assertEquals("1", cache[0]!!.id)
        assertEquals("2", cache[1]!!.id)

        cache.add(obj3)
        assertEquals(3, cache.size())

        assertEquals("1", cache[0]!!.id)
        assertEquals("2", cache[1]!!.id)
        assertEquals("3", cache[2]!!.id)
    }
}
