package ch.bailu.aat.services.directory;

import java.io.Closeable;
import java.io.File;

import android.app.Activity;
import android.widget.EditText;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppDialog;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.ServiceContext;

public class DirectoryServiceHelper implements Closeable {

    private final ServiceContext scontext;
    private final File directory;
    private String selection="";

    public DirectoryServiceHelper(ServiceContext sc, File d) {
        scontext = sc;
        directory = d;
    }



    public void setSelectionString(String s) {
        selection = s;
    }
    
    public void reopen() {
        scontext.getDirectoryService().reopen(directory, selection);
    }

    
    public void requery(String s) {
        selection = s;
        scontext.getDirectoryService().reopen(directory, selection); // or requery ????
    }


    public void rescan() {
        scontext.getDirectoryService().rescan();
    }


    public void refreshSelected() {
        scontext.getDirectoryService().deleteCurrentTrackFromDb();
    }


    public void deleteSelected(Activity activity) {
        new FileDeletionDialog(activity);
    }


    private class FileDeletionDialog extends AppDialog {
        private String fileToDelete = null;

        public FileDeletionDialog(Activity activity) {
            fileToDelete = scontext.getDirectoryService().getCurrent().getPath();
            displayYesNoDialog(activity, activity.getString(R.string.file_delete_ask), fileToDelete);
        }

        @Override
        protected void onPositiveClick() {
            new File(fileToDelete).delete();
            scontext.getDirectoryService().rescan();
        }
    }


    @Override
    public void close() {}



    public void renameSelectedFile(Activity activity) {
        new FileRenameDialog(activity);

    }

    private class FileRenameDialog extends AppDialog {
        private String file = null;
        private EditText edit;
        private Activity activity;


        public FileRenameDialog(Activity a) {
            activity = a;
            String title = activity.getString(R.string.file_rename);
            file = scontext.getDirectoryService().getCurrent().getName();
            title = title + " " + file;

            edit = new EditText(activity);
            edit.setText(file);
            displayTextDialog(activity, title, edit);
        }

        @Override
        protected void onPositiveClick() {
            File source = new File (directory, file);
            File target = new File (directory, edit.getText().toString());

            if (source.exists()) {
                if (target.exists()) {
                    AppLog.i(activity, target.getName() + " allready exists!*");
                } else {
                    source.renameTo(target);
                    rescan();
                }
            }
        }
    }
}
