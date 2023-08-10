package ch.bailu.aat_lib.util.fs;

import java.io.IOException;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile;
import ch.bailu.foc.Foc;

public class FileAction {
    public static void rescanDirectory(AppContext context, Foc file) {
        if (isParentActive(context, file)) {
            context.getServices().getDirectoryService().rescan();
        }
    }

    public static boolean isParentActive(AppContext context, Foc file) {
        final Foc currentDir = new SolidDirectoryQuery(context.getStorage(), context).getValueAsFile();
        final Foc dir = file.parent();

        return dir != null && dir.equals(currentDir);
    }

    public static void reloadPreview(AppContext context, Foc file) {
        if (isParentActive(context, file)) {
            context.getServices().getDirectoryService().deleteEntry(file);
        }
    }

    public static void useForMockLocation(AppContext context, Foc file) {
        if (file.canRead())
            new SolidMockLocationFile(context.getStorage()).setValue(file.getPath());
        else
            AFile.logErrorNoAccess(file);
    }

    public static void copyToDir(AppContext context, Foc src, Foc destDir, String p, String ext) {
        try {
            copyToDest(context, src, AppDirectory.generateUniqueFilePath(destDir,p,ext));
        } catch (IOException e) {
            AppLog.e(context, e);
        }
    }


    private static void copyToDest(AppContext context, Foc src, Foc dest) throws IOException {
        if (src != null && dest != null) {
            if (dest.exists()) {
                AFile.logErrorExists(dest);

            } else {
                src.copy(dest);
                context.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_ONDISK, dest.getPath(), src.getPath());
            }
        }
    }

    public static void copyToDir(AppContext context, Foc src, Foc destDir) {
        try {
            copyToDest(context, src, destDir.child(src.getName()));
        } catch (IOException e) {
            AppLog.e(context, e);
        }
    }

    public static void rename(AppContext context, Foc source, Foc target) {
        if (source.exists()) {
            if (target.exists()) {
                AFile.logErrorExists(target);
            } else {
                source.mv(target);
                rescanDirectory(context, source);
            }
        }
    }
}
