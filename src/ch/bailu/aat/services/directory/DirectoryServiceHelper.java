package ch.bailu.aat.services.directory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.widget.EditText;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppDialog;
import ch.bailu.aat.helpers.AppLog;

public class DirectoryServiceHelper implements Closeable {

    private final DirectoryService service;
    private final File directory;
    private String selection;

    public DirectoryServiceHelper(DirectoryService s, File d, String sel) throws IOException {
        service = s;
        directory = d;
        selection=sel;


        service.setDirectory(directory, selection);
    }



    public DirectoryServiceHelper(DirectoryService  s, File d) throws IOException {
        service = s;
        directory = d;
    }


    public void setSelection(String s) {
        selection = s;
        service.setSelection(selection);
    }


    public void rescanDirectory() {
        service.setDirectory(directory, selection);
    }


    public void refreshSelectedEntry() {
        service.deleteCurrentTrackFromDb();
        rescanDirectory();
    }


    public void deleteSelectedFile(Activity activity) {
        new FileDeletionDialog(activity);
    }


    private class FileDeletionDialog extends AppDialog {
        private String fileToDelete = null;

        public FileDeletionDialog(Activity activity) {
            fileToDelete = service.getCurrent().getPath();
            displayYesNoDialog(activity, activity.getString(R.string.file_delete_ask), fileToDelete);
        }

        @Override
        protected void onPositiveClick() {
            new File(fileToDelete).delete();
            service.setDirectory(directory, selection);
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
            file = service.getCurrent().getName();
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
                    service.setDirectory(directory, selection);
                }
            }
        }
    }
}
