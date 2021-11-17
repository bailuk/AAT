package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public class SummaryConfig {

    public Foc getSummaryDir(Foc dir) {
        return dir.child(AppDirectory.DIR_CACHE);
    }

    public String getDBPath(Foc dir) {
        return getSummaryDir(dir)
                .child(AppDirectory.FILE_CACHE_DB)
                .toString();
    }

    public Foc getPreviewFile(Foc gpxFile) {
        String name = gpxFile.getName();
        Foc dir = gpxFile.parent();

        dir = getSummaryDir(dir);

        return dir.child(name + AppDirectory.PREVIEW_EXTENSION);
    }
}
