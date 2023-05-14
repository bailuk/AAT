package ch.bailu.aat_lib.service.cache.gpx;

import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxInformationProvider;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;
import ch.bailu.aat_lib.service.cache.Obj;

public class GpxHandler implements GpxInformationProvider {
    private ObjGpx handle = ObjGpxStatic.NULL;
    private String fileID = "";
    private boolean enabled = false;

    public void disable() {
        enabled = false;
        handle.free();
        handle = ObjGpxStatic.NULL;
    }

    public ObjGpx get() {
        return handle;
    }

    public void setFileID(CacheServiceInterface cacheService, String fileID) {
        this.fileID = fileID;
        update(cacheService);
    }

    public void enable(CacheServiceInterface cacheService) {
        enabled = true;
        update(cacheService);
    }

    private void update(CacheServiceInterface cacheService) {
        ObjGpx newHandle = ObjGpx.NULL;
        if (enabled && ! "".equals(fileID)) {
            newHandle = getObjectSave(cacheService, fileID);
        }

        handle.free();
        handle = newHandle;
    }

    private ObjGpx getObjectSave(CacheServiceInterface cacheService, String id) {
        Obj handler = cacheService.getObject(id, new ObjGpxStatic.Factory());
        if (!(handler instanceof ObjGpx)) {
            handler = ObjGpx.NULL;
        }
        return (ObjGpx) handler;
    }

    @Override
    public GpxInformation getInfo() {
        return new GpxFileWrapper(handle.getFile(), getList());
    }

    private GpxList getList() {
        if (handle.isReadyAndLoaded()) {
            return handle.getGpxList();
        } else {
            return GpxList.NULL_ROUTE;
        }
    }
}
