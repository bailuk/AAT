package ch.bailu.aat.util.fs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog;
import ch.bailu.aat.views.preferences.AddOverlayDialog;
import ch.bailu.util_java.foc.Foc;

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
        final Foc currentDir = new SolidDirectoryQuery(context).getValueAsFile();
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
            AFile.logErrorReadOnly(scontext.getContext(), file);
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
            new SolidMockLocationFile(context).setValue(file.getPath());
        else
            AFile.logErrorNoAccess(context, file);
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
        copyToDir(context, src, null, null);
    }

    public static void copyToDir(Context context, Foc src,
                                 String targetPrefix, String targetExtendsion) {
        new AppSelectDirectoryDialog(context, src, targetPrefix, targetExtendsion);
    }


    public static void copyToDir(Context context, Foc src, Foc destDir, String prefix, String extension) {
        try {
            final Foc dest;

            if (prefix != null)
                dest = AppDirectory.generateUniqueFilePath(destDir, prefix, extension);

            else
                dest = destDir.child(src.getName());


            if (dest == null || dest.exists()) {
                AFile.logErrorExists(context, dest);

            } else {
                src.copy(dest);
                AppBroadcaster.broadcast(context,
                        AppBroadcaster.FILE_CHANGED_ONDISK, dest, src.getPath());
                //AppLog.i(context, dest.getPathName());
            }
        } catch (Exception e) {
            AppLog.e(context,e);
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
                            AFile.logErrorExists(activity, target);
                        } else {
                            source.mv(target);
                            rescanDirectory(scontext, file);
                        }
                    }

                }
            }.displayTextDialog(activity, title, edit);
        } else {
            AFile.logErrorReadOnly(context, file);
        }
    }



}
