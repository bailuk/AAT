package ch.bailu.aat.services.directory;

import android.database.Cursor;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;

public class GpxInformationDbEntryAndFile extends GpxInformationDbEntry implements Closeable {

    private ObjectHandle handle = ObjectHandle.NULL;
    private final ServiceContext scontext;
    
    public GpxInformationDbEntryAndFile(ServiceContext sc, Cursor c) {
        super(c);
        scontext=sc;
    }

    
    @Override
    public GpxList getGpxList() {
        if (isLoaded()) 
            return ((GpxObject)handle).getGpxList();
        
        else return super.getGpxList();
    }
    
    
    @Override
    public boolean isLoaded() {
        ObjectHandle oldHandle = handle;

        handle = scontext.getCacheService().getObject(getPath(), new GpxObjectStatic.Factory());
        oldHandle.free();
        
        if (GpxObject.class.isInstance(handle))
            return handle.isReady();
        
        return false;
    }


    @Override
    public void close() {
        handle.free();
    }

    

}
