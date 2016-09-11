package ch.bailu.aat.services.directory;

import java.io.Closeable;
import java.io.IOException;

import android.database.Cursor;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.ServiceContext;

public class IteratorFollowFile extends IteratorAbstract {
    private static Closeable NULL_CLOSEABLE = new Closeable() {
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
    public void onCursorChanged(Cursor cursor, String fid) {
        try {
            toClose.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        AppLog.d(this, "onCursorChanged");
        GpxInformationDbEntryAndFile info = new GpxInformationDbEntryAndFile(scontext, cursor);
        toClose = info;
        this.info = info;
        findFile(fid);
    }


    public boolean findFile(String fID) { 
        int old_position = getPosition();

        moveToPosition(-1);
        while ( moveToNext()) {
            if (info.getPath().equals(fID)) {
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
