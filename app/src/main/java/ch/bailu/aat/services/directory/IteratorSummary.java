package ch.bailu.aat.services.directory;

import android.database.Cursor;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.simpleio.foc.Foc;

public class IteratorSummary extends IteratorAbstract {

    private GpxInformation info = GpxInformation.NULL;

    public IteratorSummary(ServiceContext sc) {
        super(sc);
        query();
    }

    @Override
    public GpxInformation getInfo() {
        return info;
    }


    @Override
    public int getInfoID() {
            return InfoID.LIST_SUMMARY;
    }

    @Override
    public void onCursorChanged(Cursor cursor, Foc directory, String fid) {
            info = new GpxInformationDbSummary(directory, cursor);
    }

}
