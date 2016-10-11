package ch.bailu.aat.menus;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.Clipboard;
import ch.bailu.aat.helpers.FileAction;

public class FileMenu extends AbsMenu {
    private final  FileAction file;
    
    private MenuItem send, view, rename, copy, delete, overlay, reload, mock, clipboard;
    
    
    public FileMenu(FileAction f) {
        file = f;
    }
    
    
    @Override
    public void inflate(Menu menu) {
        send = menu.add(R.string.file_send);
        view = menu.add(R.string.file_view);
        rename = menu.add(R.string.file_rename);
        copy = menu.add(R.string.file_copy);
        delete = menu.add(R.string.file_delete);
        overlay = menu.add(R.string.file_overlay);
        reload = menu.add(R.string.file_reload);
        mock = menu.add(R.string.file_mock);
        clipboard = menu.add(R.string.clipboard_copy);
    }

    @Override
    public void inflateWithHeader(ContextMenu menu) {
        menu.setHeaderTitle(file.getName()); 
        inflate(menu);
    }

    @Override
    public void prepare(Menu menu) {
        
    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == delete) {
            file.delete();

        } else if (item == reload) {
            file.reloadPreview();

        } else if (item == rename) {
            file.rename();

        } else if (item == overlay) {
            file.useAsOverlay();


        } else if (item == mock) {
            file.useForMockLocation();

        } else if (item == send) {
            file.sendTo();

        } else if (item == view) {
            file.view();

        } else if (item == copy) {
            file.copyTo();

        } else if (item == clipboard) {
            file.copyToClipboard();
        } else  {
            return false;
        }
        return true;
    }

}
