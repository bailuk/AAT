package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.InfoID;

public class FileViewSource extends FileSource {

    public FileViewSource(AppContext appContext) {
        super(appContext, InfoID.FILEVIEW);
    }

    public FileViewSource(AppContext appContext, String fileID) {
        super(appContext, InfoID.FILEVIEW);
        setFileID(fileID);
    }

    @Override
    public void setFileID(String fileID) {
        super.setFileID(fileID);
        enable();
    }
}
