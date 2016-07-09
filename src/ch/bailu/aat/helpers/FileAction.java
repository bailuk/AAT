package ch.bailu.aat.helpers;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsServiceLink;
import ch.bailu.aat.preferences.AddOverlayDialog;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.ServiceContext;

public class FileAction  implements OnMenuItemClickListener {
        final File file;
        final Activity activity;
        final ServiceContext scontext;

        
        public FileAction(AbsServiceLink l) {
            this(l, new File(l.getServiceContext().getDirectoryService().getCurrent().getPath()));
        }

        public FileAction(AbsServiceLink l, File f) {
            file = f;
            activity = l;
            scontext = l.getServiceContext();
        }
        
        
        public void showPopupMenu(View v) {
            final PopupMenu popup = new PopupMenu(activity, v);
            final MenuInflater inflater = popup.getMenuInflater();

            inflater.inflate(R.menu.contextmenu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        public void createFileMenu(ContextMenu menu) {
            activity.getMenuInflater().inflate(R.menu.contextmenu, menu);
            menu.setHeaderTitle(file.getName());
        }

        
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId()== R.id.m_file_delete) {
                delete();

            } else if (item.getItemId() == R.id.m_file_reload) {
                reloadPreview();

            } else if (item.getItemId() == R.id.m_file_rename) {
                rename();

            } else if (item.getItemId() == R.id.m_file_overlay) {
                useAsOverlay();
                        

            } else if (item.getItemId() == R.id.m_file_mock) {
                useForMockLocation();
                
            } else if (item.getItemId() == R.id.m_file_send) {
                sendTo();

            } else if (item.getItemId() == R.id.m_file_copy) {
                try {
                    copyTo();
                } catch (IOException e) {
                    AppLog.e(activity, e);
                    return false;
                }

            } else  {
                return false;
            }
            return true;
            
        }
        
        
        public void rescanDirectory() {
            final String directory = new File(scontext.getDirectoryService().getCurrent().getPath()).getParent();
            
            if (directory != null && directory.equals(file.getParent())) {
                scontext.getDirectoryService().rescan();
            }
        }
        
        
        public void reloadPreview() {
            final String sfile = scontext.getDirectoryService().getCurrent().getPath();
            
            if (sfile != null && sfile.equals(file)) {
                scontext.getDirectoryService().deleteCurrentTrackFromDb();
            }
        }
        
        
        public void delete() {
            new FileDeletionDialog();
        }
        
        
        
        
        private class FileDeletionDialog extends AppDialog {
            public FileDeletionDialog() {
                displayYesNoDialog(activity, activity.getString(R.string.file_delete_ask), file.toString());
            }

            
            @Override
            protected void onPositiveClick() {
                file.delete();
                rescanDirectory();
            }
        }

        
        public void useAsOverlay() {
            new AddOverlayDialog(activity, file);
        }
        
        public void useForMockLocation() {
            new SolidMockLocationFile(activity).setValue(file.toString());
        }
        
        public void sendTo() {
            AppFile.send(activity, file);
        }
        
        public void copyTo() throws IOException {
            AppFile.copyTo(activity, file);
        }
        
        public void rename() {
            new FileRenameDialog();

        }

        private class FileRenameDialog extends AppDialog {
            private final EditText edit;
            private final String directory=file.getParent();
            
            
            public FileRenameDialog() {
                final String title = activity.getString(R.string.file_rename) + " " + file.getName();

                edit = new EditText(activity);
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

}
