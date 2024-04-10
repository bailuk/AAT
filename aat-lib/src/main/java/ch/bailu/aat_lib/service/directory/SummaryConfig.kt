package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public abstract class SummaryConfig {

    public abstract Foc getSummaryDir(Foc dir);

    public abstract String getDBPath(Foc dir);

    public Foc getPreviewFile(Foc gpxFile) {
        String name = gpxFile.getName();
        Foc dir = gpxFile.parent();

        dir = getSummaryDir(dir);

        return dir.child(name + AppDirectory.PREVIEW_EXTENSION);
    }
}
