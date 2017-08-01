package ch.bailu.aat.services.directory;

import android.database.Cursor;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.simpleio.foc.Foc;

public class IteratorFollowFile extends IteratorAbstract {
    private static final Closeable NULL_CLOSEABLE = new Closeable() {
        @Override
        public void close() {}

    };


    private GpxInformation info = GpxInformation.NULL;
    private Closeable toClose = NULL_CLOSEABLE;

    private final ServiceContext scontext;

    public IteratorFollowFile(ServiceContext sc) {
        super(sc);
        scontext=sc;
        query();
    }


    @Override
    public GpxInformation getInfo() {
        return info;
    }



    @Override
    public void onCursorChanged(Cursor cursor, Foc directory, String fid) {
        try {
            toClose.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
        GpxInformationDbEntryAndFile info
                = new GpxInformationDbEntryAndFile(scontext, directory, cursor);
        toClose = info;
        this.info = info;
*/

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


    @Override
    public void close() {
        try {
            toClose.close();
        } catch (IOException e) {
            AppLog.e(scontext.getContext(), e);
        }
        super.close();
    }


}
