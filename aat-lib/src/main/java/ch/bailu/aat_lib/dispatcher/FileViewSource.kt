package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.foc.Foc;

/**
 * Display a file. Usually selected from track/overlay list
 */
public class FileViewSource extends FileSource {

    public FileViewSource(AppContext appContext) {
        super(appContext, InfoID.FILEVIEW);
    }

    public FileViewSource(AppContext appContext, Foc file) {
        super(appContext, InfoID.FILEVIEW);
        setFile(file);
    }

    @Override
    public void setFile(Foc file) {
        super.setFile(file);
        setEnabled(true);
    }
}
