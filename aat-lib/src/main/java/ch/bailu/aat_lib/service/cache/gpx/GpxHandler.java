package ch.bailu.aat_lib.service.cache.gpx;

import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxInformationProvider;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.foc.Foc;

public class GpxHandler implements GpxInformationProvider {
    private ObjGpx handle = ObjGpxStatic.NULL;
    private Foc file = GpxInformation.FOC_NULL;
    private boolean enabled = false;

    public void disable() {
        enabled = false;
        handle.free();
        handle = ObjGpxStatic.NULL;
    }

    public ObjGpx get() {
        return handle;
    }

    public void setFileID(ServicesInterface services, Foc file) {
        this.file = file;
        if (enabled) {
            update(services);
        }
    }

    public void enable(ServicesInterface services) {
        enabled = true;
        update(services);
    }

    private void update(ServicesInterface services) {
        ObjGpx newHandle = ObjGpx.NULL;
        if (enabled && ! "".equals(file.getName())) {
            newHandle = getObjectSave(services.getCacheService(), file);
        }

        handle.free();
        handle = newHandle;
    }

    private ObjGpx getObjectSave(CacheServiceInterface cacheService, Foc file) {
        Obj handler = cacheService.getObject(file.getPath(), new ObjGpxStatic.Factory());
        if (!(handler instanceof ObjGpx)) {
            handler = ObjGpx.NULL;
        }
        return (ObjGpx) handler;
    }

    @Override
    public GpxInformation getInfo() {
        return new GpxFileWrapper(file, getList());
    }

    private GpxList getList() {
        if (handle.isReadyAndLoaded()) {
            return handle.getGpxList();
        } else {
            return GpxList.NULL_ROUTE;
        }
    }
}
