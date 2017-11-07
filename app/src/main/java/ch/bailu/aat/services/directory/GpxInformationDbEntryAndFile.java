package ch.bailu.aat.services.directory;

/*
public class GpxInformationDbEntryAndFile extends GpxInformationDbEntry implements Closeable {

    private ObjectHandle handle = ObjectHandle.NULL;
    private final ServiceContext scontext;
    
    public GpxInformationDbEntryAndFile(ServiceContext sc, Foc p, Cursor c) {
        super(c, p);
        scontext=sc;
    }


    @Override
    public GpxList getGpxList() {
        if (isValid())
            return ((GpxObject)handle).getGpxList();
        
        else return super.getGpxList();
    }

    
    @Override
    public boolean isValid() {
        ObjectHandle oldHandle = handle;

        handle = scontext.getCacheService().getObject(getFile().getPath(), new GpxObjectStatic.Factory());
        oldHandle.free();
        
        if (GpxObject.class.isInstance(handle))
            return handle.isReadyAndLoaded();
        
        return false;
    }




    @Override
    public float getMaximumSpeed() {
        if (isValid()) {
            return ((GpxObject)handle).getGpxList().getDelta().getMaximumSpeed();
        }
        return super.getMaximumSpeed();
    }


    @Override
    public long getAutoPause() {

        if (isValid()) {
            return ((GpxObject)handle).getGpxList().getDelta().getAutoPause();
        }
        return super.getAutoPause();
    }

    @Override
    public long getStartTime() {
        if (isValid()) {
            return ((GpxObject)handle).getGpxList().getDelta().getStartTime();
        }
        return super.getStartTime();
    }


    @Override
    public long getEndTime() {
        if (isValid()) {
            return ((GpxObject)handle).getGpxList().getDelta().getEndTime();
        }
        return super.getEndTime();
    }

    @Override
    public short getAscend() {

        if (isValid()) {
            return ((GpxObject)handle).getGpxList().getDelta().getAscend();
        }
        return super.getAscend();
    }


    @Override
    public short getDescend() {

        if (isValid()) {
            return ((GpxObject)handle).getGpxList().getDelta().getDescend();
        }
        return super.getDescend();
    }


    @Override
    public void close() {
        handle.free();
    }


}
*/