package ch.bailu.aat_lib.service.directory;


import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.util.sql.ResultSet;
import ch.bailu.foc.Foc;

public final class IteratorSummary extends IteratorAbstract {

    private GpxInformation info = GpxInformation.NULL;

    public IteratorSummary(AppContext appContext) {
        super(appContext);
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
    public void onCursorChanged(ResultSet cursor, Foc directory, String fid) {
            info = new GpxInformationDbSummary(directory, cursor);
    }

}
