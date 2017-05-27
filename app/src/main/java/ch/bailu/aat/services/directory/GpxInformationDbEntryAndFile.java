package ch.bailu.aat.services.directory;

import android.database.Cursor;

import java.io.Closeable;
import java.io.File;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.simpleio.foc.Foc;

public class GpxInformationDbEntryAndFile extends GpxInformationDbEntry implements Closeable {

    private ObjectHandle handle = ObjectHandle.NULL;
    private final ServiceContext scontext;
    
    public GpxInformationDbEntryAndFile(ServiceContext sc, Foc p, Cursor c) {
        super(c, p);
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
            return handle.isReadyAndLoaded();
        
        return false;
    }


    @Override
    public void close() {
        handle.free();
    }


    @Override
    public float getMaximumSpeed() {

        if (isLoaded()) {
            return ((GpxObject)handle).getGpxList().getDelta().getMaximumSpeed();
        }
        return super.getMaximumSpeed();
    }

}
