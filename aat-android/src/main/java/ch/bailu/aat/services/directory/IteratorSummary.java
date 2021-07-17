package ch.bailu.aat.services.directory;

import android.database.Cursor;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.foc.Foc;

public final class IteratorSummary extends IteratorAbstract {

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
