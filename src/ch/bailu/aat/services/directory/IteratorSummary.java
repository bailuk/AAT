package ch.bailu.aat.services.directory;

import android.database.Cursor;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;

public class IteratorSummary extends IteratorAbstract {

    private GpxInformation info = GpxInformation.NULL;


    public IteratorSummary(ServiceContext sc, String s) {
        super(sc);
        query(s);
    }

    @Override
    public GpxInformation getInfo() {
        return info;
    }

    @Override
    public void onCursorChanged(Cursor cursor, String fid) {
        info = new GpxInformationDbSummary(cursor);
    }
    
}
