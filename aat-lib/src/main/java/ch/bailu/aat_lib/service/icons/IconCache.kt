package ch.bailu.aat_lib.service.icons

import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.cache.LockCache
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract
import ch.bailu.aat_lib.service.cache.icons.ObjSVGAsset
import ch.bailu.aat_lib.util.Objects
import ch.bailu.aat_lib.util.WithStatusText
import java.io.Closeable

class IconCache(private val scontext: ServicesInterface) : Closeable, WithStatusText {
    private val icons = LockCache<ObjImageAbstract>(20)
    fun getIcon(path: String?, size: Int): ObjImageAbstract? {
        val r = arrayOf<ObjImageAbstract?>(null)
        if (path != null) {
            val iconFileID = ObjSVGAsset.toID(path, size)
            var icon = get(iconFileID)
            if (icon == null) {
                icon = add(iconFileID, path, size)
            }
            r[0] = icon
        }
        return r[0]
    }

    private operator fun get(id: String): ObjImageAbstract? {
        for (i in 0 until icons.size()) {
            if (Objects.equals(id, icons[i].toString())) {
                return icons.use(i)
            }
        }
        return null
    }

    private fun add(id: String, path: String, size: Int): ObjImageAbstract? {
        var r: ObjImageAbstract? = null
        val handle = scontext.getCacheService().getObject(id, ObjSVGAsset.Factory(path, size))
        if (handle is ObjSVGAsset) {
            icons.add(handle)
            r = handle
        }
        return r
    }

    override fun close() {
        icons.close()
    }

    override fun appendStatusText(builder: StringBuilder) {
        builder.append("IconCache (icons) size: ").append(icons.size()).append("<br>")
    }
}
