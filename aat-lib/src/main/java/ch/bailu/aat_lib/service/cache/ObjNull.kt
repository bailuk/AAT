package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext

object ObjNull : Obj(ObjNull::class.java.simpleName) {
    override fun getSize(): Long {
        return MIN_SIZE.toLong()
    }

    override fun onDownloaded(id: String, url: String, sc: AppContext) {}
    override fun onChanged(id: String, sc: AppContext) {}
}
