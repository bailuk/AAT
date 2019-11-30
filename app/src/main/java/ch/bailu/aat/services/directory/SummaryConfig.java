package ch.bailu.aat.services.directory;

import android.content.Context;

import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocFile;

public final class SummaryConfig {
    public static String getWriteableDBPath(Context context, Foc dir) {
        return getWriteableSummaryDir(context, dir)
                .child(AppDirectory.FILE_CACHE_DB)
                .toString();
    }


    public static Foc getWriteableSummaryDir(Context context, Foc dir) {
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


    static public Foc getWriteablePreviewFile(Context context, Foc gpxFile)  {
        String name = gpxFile.getName();
        Foc dir = gpxFile.parent();

        dir = getWriteableSummaryDir(context, dir);

        return dir.child(name + AppDirectory.PREVIEW_EXTENSION);

    }

    public static Foc getReadablePreviewFile(Context context, Foc gpxFile) {
        return getWriteablePreviewFile(context, gpxFile);
    }
}
