package ch.bailu.aat.menus;

import android.app.Activity;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsServiceLink;
import ch.bailu.aat.helpers.file.FileAction;
import ch.bailu.aat.services.ServiceContext;

public class FileMenu extends AbsMenu {
    private final File file;
    private final Activity activity;
    private final ServiceContext scontext;
    
    private MenuItem send, view, rename, copy, delete, overlay, reload, mock, clipboard;
    
    
    public FileMenu(AbsServiceLink a, File f) {
        file = f;
        scontext = a.getServiceContext();
        activity = a;
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
            FileAction.delete(scontext, activity, file);

        } else if (item == reload) {
            FileAction.reloadPreview(scontext, file);

        } else if (item == rename) {
            FileAction.rename(scontext, activity, file);

        } else if (item == overlay) {
            FileAction.useAsOverlay(scontext.getContext(), file);


        } else if (item == mock) {
            FileAction.useAsOverlay(scontext.getContext(), file);

        } else if (item == send) {
            FileAction.sendTo(scontext.getContext(), Uri.fromFile(file));

        } else if (item == view) {
            FileAction.view(scontext.getContext(), file);

        } else if (item == copy) {
            FileAction.copyTo(scontext.getContext(), Uri.fromFile(file));

        } else if (item == clipboard) {
            FileAction.copyToClipboard(scontext.getContext(), file);
        } else  {
            return false;
        }
        return true;
    }

}
