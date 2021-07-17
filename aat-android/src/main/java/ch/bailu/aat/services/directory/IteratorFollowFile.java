package ch.bailu.aat.services.directory;

import android.database.Cursor;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.foc.Foc;

public final class IteratorFollowFile extends IteratorAbstract {
    private GpxInformation info = GpxInformation.NULL;


    //private final ServiceContext scontext;

    public IteratorFollowFile(ServiceContext sc) {
        super(sc);
        //scontext=sc;
        query();
    }


    @Override
    public GpxInformation getInfo() {
        return info;
    }



    @Override
    public void onCursorChanged(Cursor cursor, Foc directory, String fid) {
        info = new GpxInformationDbEntry(cursor, directory);

        findFile(fid);
    }


    public boolean findFile(String fID) {
        int old_position = getPosition();

        moveToPosition(-1);
        while ( moveToNext()) {
            if (info.getFile().getPath().equals(fID)) {
                return true;
            }
        }

        moveToPosition(old_position);
        return false;
    }

}
