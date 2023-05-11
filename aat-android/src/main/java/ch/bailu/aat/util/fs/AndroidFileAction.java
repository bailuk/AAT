package ch.bailu.aat.util.fs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog;
import ch.bailu.aat.views.preferences.AddOverlayDialog;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.util.fs.AFile;
import ch.bailu.aat_lib.util.fs.FileAction;
import ch.bailu.foc.Foc;

public class AndroidFileAction {



    public static void delete(final AppContext context,
                              final Activity activity,
                              final Foc file) {

        if (file.canWrite()) {
            new AppDialog() {
                @Override
                protected void onPositiveClick() {
                    file.rm();
                    FileAction.rescanDirectory(context, file);
                }
            }.displayYesNoDialog(activity,
                    Res.str().file_delete_ask(),
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


    public static void copyToDir(Context context, AppContext appContext, Foc src) {
        new AppSelectDirectoryDialog(context, src) {
            @Override
            public void copyTo(Context _context, Foc srcFile, Foc destDirectory) {
                try {

                    FileAction.copyToDir(appContext, srcFile, destDirectory);
                } catch (Exception e) {
                    AppLog.e(appContext, e);
                }

            }
        };
    }

    public static void rename(final AppContext context, final Activity activity, final Foc file) {

        if (file.canWrite() && file.hasParent()) {
            final Foc directory = file.parent();

            final String title = Res.str().file_rename() + " " + file.getName();
            final EditText edit = new EditText(activity);
            edit.setText(file.getName());

            new AppDialog() {

                @Override
                protected void onPositiveClick() {
                    Foc source = directory.child(file.getName());
                    Foc target = directory.child(edit.getText().toString());

                    FileAction.rename(context, source, target);

                }
            }.displayTextDialog(activity, title, edit);
        } else {
            AFile.logErrorReadOnly(file);
        }
    }

}
