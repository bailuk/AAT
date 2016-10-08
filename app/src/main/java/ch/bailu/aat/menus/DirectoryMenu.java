package ch.bailu.aat.menus;


import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.Clipboard;
import ch.bailu.aat.helpers.file.FileIntent;
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
        FileIntent intent = new FileIntent(sdirectory.getValueAsFile());

        if (item == view) {
            intent.view(sdirectory.getContext());

        } else if (item == get) {
            intent.pick(sdirectory.getContext());

        } else if (item == clipboard) {
            new Clipboard(sdirectory.getContext()).setText(sdirectory.getLabel(),
                    sdirectory.getValueAsString());

        } else {
            return false;
        }
        return true;
    }

}
