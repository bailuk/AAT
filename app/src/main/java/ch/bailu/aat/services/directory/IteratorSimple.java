package ch.bailu.aat.services.directory;

import android.database.Cursor;

import java.io.File;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;

public class IteratorSimple extends IteratorAbstract {
    private GpxInformation info = GpxInformation.NULL;

    
    public IteratorSimple(ServiceContext sc) {
        super(sc);
        query();
    }

    
    @Override
    public GpxInformation getInfo() {
        return info;
    }

    
    @Override
    public void onCursorChanged(Cursor cursor, File directory, String fid) {
        if (cursor.getCount()>0) {
            info = new GpxInformationDbEntry(cursor, directory);
        } else {
            info = GpxInformation.NULL;
        }
        
    }
}
