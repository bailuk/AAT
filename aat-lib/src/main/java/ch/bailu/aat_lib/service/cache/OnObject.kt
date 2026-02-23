package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext

abstract class OnObject(
    appContext: AppContext, id: String, c: Class<*>,
    factory: Obj.Factory? = null
) {
    init {
        appContext.services.insideContext {
            val handle =if (factory == null) {
                appContext.services.getCacheService().getObject(id)
            } else {
                appContext.services.getCacheService().getObject(id, factory)
            }

            try {
                if (c.isInstance(handle)) this@OnObject.run(handle)
            } finally {
                handle.free()
            }
        }
    }

    abstract fun run(handle: Obj)
}
