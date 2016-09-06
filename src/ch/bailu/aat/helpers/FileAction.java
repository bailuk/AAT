package ch.bailu.aat.helpers;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsServiceLink;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.preferences.AddOverlayDialog;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.directory.Iterator;

public class FileAction   {
    private final File file;
    private final Activity activity;
    private final ServiceContext scontext;


    public FileAction(AbsServiceLink l, Iterator iterator) {
        this(l, new File(iterator.getInfo().getPath()));
    }


    public FileAction(AbsServiceLink l, File f) {
        file = f;
        activity = l;
        scontext = l.getServiceContext();
    }


  
    public void rescanDirectory() {
        if (file.getParent().equals(new SolidDirectory(activity).getValue())) {
            scontext.getDirectoryService().rescan();
        }
    }


    public void reloadPreview() {
        if (file.getParent().equals(new SolidDirectory(scontext.getContext()).getValue())) {
            scontext.getDirectoryService().deleteEntry(file.getAbsolutePath());
        }
    }


    public void delete() {
        new FileDeletionDialog();
    }


    private class FileDeletionDialog extends AppDialog {
        public FileDeletionDialog() {
            displayYesNoDialog(activity, scontext.getContext().getString(R.string.file_delete_ask), file.toString());
        }


        @Override
        protected void onPositiveClick() {
            file.delete();
            rescanDirectory();
        }
    }


    public void useAsOverlay() {
        new AddOverlayDialog(scontext.getContext(), file);
    }

    public void useForMockLocation() {
        new SolidMockLocationFile(scontext.getContext()).setValue(file.toString());
    }

    
    public void view() {
        AppFile.view(scontext.getContext(), file);
    }
    
    
    public void sendTo() {
        AppFile.send(scontext.getContext(), file);
    }

    public void copyTo() {
        try {
            AppFile.copyTo(scontext.getContext(), file);
        } catch (IOException e) {
            AppLog.e(scontext.getContext(), e);
        }
    }

    public void rename() {
        new FileRenameDialog();

    }

    private class FileRenameDialog extends AppDialog {
        private final EditText edit;
        private final String directory=file.getParent();


        public FileRenameDialog() {
            final String title = scontext.getContext().getString(R.string.file_rename) + " " + file.getName();

            edit = new EditText(scontext.getContext());
            edit.setText(file.getName());
            displayTextDialog(activity, title, edit);
        }

        @Override
        protected void onPositiveClick() {
            File source = new File (directory, file.getName());
            File target = new File (directory, edit.getText().toString());

            if (source.exists()) {
                if (target.exists()) {
                    AppLog.i(activity, target.getName() + " allready exists!*");
                } else {
                    source.renameTo(target);
                    rescanDirectory();
                }
            }
        }
    }


    public CharSequence getName() {
        return file.getName();
    }


    public void showPopupMenu(View v) {
        new FileMenu(this).showAsPopup(activity, v);
    }

}
