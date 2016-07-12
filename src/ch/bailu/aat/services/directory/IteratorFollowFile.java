package ch.bailu.aat.services.directory;

import android.database.Cursor;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;

public class IteratorFollowFile extends Iterator {
    private GpxInformation info = GpxInformation.NULL;
    
    private final ServiceContext scontext;
    
    public IteratorFollowFile(ServiceContext sc, String s) {
        super(sc, s);
        scontext=sc;
    }



    @Override
    public GpxInformation getInfo() {
        return info;
    }



    @Override
    public void onCursorChanged(Cursor cursor, String fid) {
        info = new GpxInformationDbEntryAndFile(scontext, cursor);

        findFile(fid);
    }

    
    public boolean findFile(String fID) { 
        int old_position = getPosition();
        
        
        if (moveToPosition(0)) {
            do {
                if (info.getPath().equals(fID)) {
                    return true;
                }
            }
            while(moveToNext());
        }
        moveToPosition(old_position);
        return false;
    }
}
