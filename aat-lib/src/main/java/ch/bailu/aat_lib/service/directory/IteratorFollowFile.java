package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.util.sql.ResultSet;
import ch.bailu.foc.Foc;

public final class IteratorFollowFile extends IteratorAbstract {
    private GpxInformation info = GpxInformation.NULL;

    public IteratorFollowFile(AppContext appContext) {
        super(appContext);
        query();
    }


    @Override
    public GpxInformation getInfo() {
        return info;
    }


    @Override
    public void onCursorChanged(ResultSet resultSet, Foc directory, String fid) {
        info = new GpxInformationDbEntry(resultSet, directory);

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
