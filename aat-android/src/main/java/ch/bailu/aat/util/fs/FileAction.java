package ch.bailu.aat.util.fs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog;
import ch.bailu.aat.views.preferences.AddOverlayDialog;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile;
import ch.bailu.aat_lib.util.fs.AFile;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroidFactory;

public class FileAction {

    public static void rescanDirectory(ServiceContext scontext, Foc file) {
        if (isParentActive(scontext.getContext(), file)) {
            scontext.getDirectoryService().rescan();
        }
    }


    public static void reloadPreview(ServiceContext scontext, Foc file) {
        if (isParentActive(scontext.getContext(), file)) {
            scontext.getDirectoryService().deleteEntry(file);
        }
    }


    public static boolean isParentActive(Context context, Foc file) {
        final Foc currentDir = new SolidDirectoryQuery(new Storage(context), new FocAndroidFactory(context)).getValueAsFile();
        final Foc dir = file.parent();


        return dir != null && dir.equals(currentDir);
    }


    public static void delete(final ServiceContext scontext,
                              final Activity activity,
                              final Foc file) {

        if (file.canWrite()) {
            new AppDialog() {
                @Override
                protected void onPositiveClick() {
                    file.rm();
                    rescanDirectory(scontext, file);
                }
            }.displayYesNoDialog(activity,
                    scontext.getContext().getString(R.string.file_delete_ask),
                    file.getPathName());
        } else {
            AFile.logErrorReadOnly(file);
        }
    }


    public static void copyToClipboard(Context context, Uri uri) {
        copyToClipboard(uri.getLastPathSegment(), uri.toString(), context);
    }

    public static void copyToClipboard(Context context, Foc file) {
        copyToClipboard(file.getName(), file.toString(), context);

    }

    private static void copyToClipboard(String label, String content, Context context) {
        if (label != null && content != null)
            new Clipboard(context).setText(label, content);

    }

    public static void useAsOverlay(Context context, Foc file) {
        new AddOverlayDialog(context, file);
    }

    public static void useForMockLocation(Context context, Foc file) {
        if (file.canRead())
            new SolidMockLocationFile(new Storage(context)).setValue(file.getPath());
        else
            AFile.logErrorNoAccess(file);
    }


    public static void view(Context context, Uri uri) {
        FileIntent.view(context, new Intent(), uri);
    }


    public static void view(Context context, Foc file) {
        FileIntent.view(context, new Intent(), file);
    }

    public static void sendTo(Context context, Foc file) {
        FileIntent.send(context, new Intent(), file);
    }


    public static void sendTo(Context context, Uri uri) {
        FileIntent.send(context, new Intent(), uri);
    }


    public static void copyToDir(Context context, Foc src) {
        new AppSelectDirectoryDialog(context, src) {
            @Override
            public void copyTo(Context context, Foc srcFile, Foc destDirectory) {
                try {

                    FileAction.copyToDir(context, srcFile, destDirectory);
                } catch (Exception e) {
                    AppLog.e(context, e);
                }

            }
        };
    }

    public static void copyToDir(Context context, Foc src, Foc destDir, String p, String ext) {
        try {
            copyToDest(context, src, AppDirectory.generateUniqueFilePath(destDir,p,ext));
        } catch (IOException e) {
            AppLog.e(context, e);
        }
    }

    public static void copyToDir(Context context, Foc src, Foc destDir) {
        try {
            copyToDest(context, src, destDir.child(src.getName()));
        } catch (IOException e) {
            AppLog.e(context, e);
        }
    }


    private static void copyToDest(Context context, Foc src, Foc dest) throws IOException {
        if (src != null && dest != null) {
            if (dest.exists()) {
                AFile.logErrorExists(dest);

            } else {
                src.copy(dest);
                OldAppBroadcaster.broadcast(context,
                        AppBroadcaster.FILE_CHANGED_ONDISK, dest, src.getPath());
            }
        }
    }


    public static void rename(final ServiceContext scontext, final Activity activity, final Foc file) {
        final Context context = scontext.getContext();

        if (file.canWrite() && file.hasParent()) {
            final Foc directory = file.parent();

            final String title = context.getString(R.string.file_rename) + " " + file.getName();
            final EditText edit = new EditText(context);
            edit.setText(file.getName());

            new AppDialog() {

                @Override
                protected void onPositiveClick() {
                    Foc source = directory.child(file.getName());
                    Foc target = directory.child(edit.getText().toString());

                    if (source.exists()) {
                        if (target.exists()) {
                            AFile.logErrorExists(target);
                        } else {
                            source.mv(target);
                            rescanDirectory(scontext, file);
                        }
                    }

                }
            }.displayTextDialog(activity, title, edit);
        } else {
            AFile.logErrorReadOnly(file);
        }
    }

}
