package ch.bailu.aat.menus;


import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.fs.FileIntent;
import ch.bailu.aat.preferences.SolidDirectory;

public class DirectoryMenu extends AbsMenu {
    private MenuItem view, get, clipboard;


    private final SolidDirectory sdirectory;
    public DirectoryMenu(SolidDirectory d) {
        sdirectory = d;
    }


    @Override
    public void inflate(Menu menu) {
        view = menu.add(R.string.file_view);
        get = menu.add(R.string.file_view);
        clipboard = menu.add(R.string.clipboard_copy);
    }

    @Override
    public void inflateWithHeader(ContextMenu menu) {
        menu.setHeaderTitle(sdirectory.getValueAsFile().getName());
        menu.setHeaderIcon(R.drawable.folder_inverse);
        inflate(menu);
    }

    @Override
    public void prepare(Menu menu) {

    }

    @Override
    public boolean onItemClick(MenuItem item) {


        if (item == view) {
            FileIntent.view(sdirectory.getContext(), new Intent(), sdirectory.getValueAsFile());

        } else if (item == get) {
            FileIntent.browse(sdirectory.getContext(), new Intent(),
                    Uri.fromFile(sdirectory.getValueAsFile()));

        } else if (item == clipboard) {
            new Clipboard(sdirectory.getContext()).setText(sdirectory.getLabel(),
                    sdirectory.getValueAsString());

        } else {
            return false;
        }
        return true;
    }

}
