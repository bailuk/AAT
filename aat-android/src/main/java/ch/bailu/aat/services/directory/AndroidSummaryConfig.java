package ch.bailu.aat.services.directory;

import android.content.Context;

import ch.bailu.aat_lib.service.directory.SummaryConfig;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFile;

public final class AndroidSummaryConfig extends SummaryConfig {
    private final Context context;

    public AndroidSummaryConfig(Context context) {
        this.context = context;
    }

    @Override
    public Foc getSummaryDir(Foc dir) {
        Foc summaryDir;

        if (dir instanceof FocFile && dir.mkdirs() && dir.canWrite()) {
            summaryDir = dir.child(AppDirectory.DIR_CACHE);

        } else {

            summaryDir = new FocFile(context.getCacheDir())
                    .child("summary")
                    .child(String.valueOf(dir.hashCode()));
        }
        summaryDir.mkdirs();

        return summaryDir;

    }

    @Override
    public String getDBPath(Foc dir) {
        return getSummaryDir(dir)
                .child(AppDirectory.FILE_CACHE_DB)
                .toString();
    }
}
