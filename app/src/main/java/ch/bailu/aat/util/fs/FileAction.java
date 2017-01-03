package ch.bailu.aat.util.fs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.preferences.AddOverlayDialog;

public class FileAction   {

    public static void rescanDirectory(ServiceContext scontext, File file) {
        if (isParentActiveAndWriteable(scontext.getContext(), file)) {
            scontext.getDirectoryService().rescan();
        }
    }


    public static void reloadPreview(ServiceContext scontext, File file) {
        if (isParentActiveAndWriteable(scontext.getContext(), file)) {
            scontext.getDirectoryService().deleteEntry(file);
        }
    }


    public static boolean isParentActiveAndWriteable(Context context, File file) {
        final File currentDir = new SolidDirectoryQuery(context).getValueAsFile();
        final File dir = file.getParentFile();

        return dir.canWrite() && dir.equals(currentDir);
    }


    public static void delete(final ServiceContext scontext,
                              final Activity activity,
                              final File file) {

        if (file.canWrite()) {
            new AppDialog() {
                @Override
                protected void onPositiveClick() {
                    file.delete();
                    rescanDirectory(scontext, file);
                }
            }.displayYesNoDialog(activity,
                    scontext.getContext().getString(R.string.file_delete_ask),
                    file.toString());
        } else {
            FileUI.logReadOnly(scontext.getContext(), file);
        }
    }


    public static void copyToClipboard(Context context, Uri uri) {
        copyToClipboard(uri.getLastPathSegment(), uri.toString(), context);
    }

    public static void copyToClipboard(Context context, File file) {
        copyToClipboard(file.getName(), file.getAbsolutePath(), context);

    }

    private static void copyToClipboard(String label, String content, Context context) {
        if (label != null && content != null)
            new Clipboard(context).setText(label, content);

    }

    public static void useAsOverlay(Context context, File file) {
        new AddOverlayDialog(context, file);
    }

    public static void  useForMockLocation(Context context, File file) {
        if (file.canRead())
            new SolidMockLocationFile(context).setValue(file.toString());
        else
            FileUI.logNoAccess(context, file);
    }

    
    public static void view(Context context, Uri uri) {
        FileIntent.view(context, new Intent(), uri);
    }


    public static void view(Context context, File file) {
        FileIntent.view(context, new Intent(), file);
    }
    
    public static void sendTo(Context context, File file) {
        FileIntent.send(context, new Intent(), file);
    }


    public static void sendTo(Context context, Uri uri) {
        FileIntent.send(context, new Intent(), uri);
    }


    public static void copyTo(Context context, Uri uri) {
        new AppSelectDirectoryDialog(context, uri);
    }

    public static void copyTo(Context context, Uri uri, File targetDir) throws Exception {
        final File target = new File(targetDir, uri.getLastPathSegment());

        if (target.exists()) {
            FileUI.logExists(context, target);
        } else {
            new UriAccess(context, uri).copy(target);
            AppLog.i(context, target.getAbsolutePath());
        }
    }





    public static void rename(final ServiceContext scontext, final Activity activity, final File file) {
        final Context context = scontext.getContext();

        if (file.canWrite()) {
            final String directory=file.getParent();

            final String title = context.getString(R.string.file_rename) + " " + file.getName();
            final EditText edit = new EditText(context);
            edit.setText(file.getName());

            new AppDialog() {

                @Override
                protected void onPositiveClick() {
                    File source = new File (directory, file.getName());
                    File target = new File (directory, edit.getText().toString());

                    if (source.exists()) {
                        if (target.exists()) {
                            FileUI.logExists(activity, target);
                        } else {
                            source.renameTo(target);
                            rescanDirectory(scontext, file);
                        }
                    }

                }
            }.displayTextDialog(activity, title, edit);
        } else {
            FileUI.logReadOnly(context, file);
        }
    }
}
