package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.information.GpxInformation;
import ch.bailu.aat_lib.util.sql.DbResultSet;
import ch.bailu.foc.Foc;

public final class IteratorSimple extends IteratorAbstract {
    private GpxInformation info = GpxInformation.NULL;

    public IteratorSimple(AppContext appContext) {
        super(appContext);
        query();
    }

    @Override
    public GpxInformation getInfo() {
        return info;
    }

    @Override
    public void onCursorChanged(DbResultSet cursor, Foc directory, String fid) {
        if (cursor.getCount()>0) {
            info = new GpxInformationDbEntry(cursor, directory);
        } else {
            info = GpxInformation.NULL;
        }

    }
}
