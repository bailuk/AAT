package ch.bailu.aat_lib.service.cache.gpx

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.information.GpxFileWrapper
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.GpxInformationProvider
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.foc.Foc

class GpxHandler : GpxInformationProvider {
    private var handle: ObjGpx = ObjGpx.NULL
    private var file: Foc = Foc.FOC_NULL
    private var enabled = false

    fun disable() {
        enabled = false
        handle.free()
        handle = ObjGpx.NULL
    }

    fun get(): ObjGpx {
        return handle
    }

    fun setFileID(services: ServicesInterface, file: Foc) {
        this.file = file
        if (enabled) {
            update(services)
        }
    }

    fun enable(services: ServicesInterface) {
        enabled = true
        update(services)
    }

    private fun update(services: ServicesInterface) {
        var newHandle = ObjGpx.NULL
        if (enabled && "" != file.name) {
            newHandle = getObjectSave(services.getCacheService(), file)
        }

        handle.free()
        handle = newHandle
    }

    private fun getObjectSave(cacheService: CacheServiceInterface, file: Foc): ObjGpx {
        var handler = cacheService.getObject(file.path, ObjGpxStatic.Factory())
        if (handler !is ObjGpx) {
            handler = ObjGpx.NULL
        }
        return handler
    }

    override fun getInfo(): GpxInformation {
        return GpxFileWrapper(file, getList())
    }

    private fun getList(): GpxList {
        return if (handle.isReadyAndLoaded()) {
            handle.getGpxList()
        } else {
            GpxList.NULL_ROUTE
        }
    }
}
