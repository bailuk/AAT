package ch.bailu.aat.menus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.fs.FileIntent;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;

public class DirectoryMenu extends AbsMenu {
    private MenuItem view, get, clipboard, permission;

    private final Activity acontext;

    private final SolidFile sdirectory;
    public DirectoryMenu(Activity c, SolidFile d) {
        acontext = c;
        sdirectory = d;
    }


    @Override
    public void inflate(Menu menu) {
        view = menu.add(R.string.file_view);
        get = menu.add(R.string.file_view);
        clipboard = menu.add(R.string.clipboard_copy);
        permission = menu.add("Ask for permission*");
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
        Context c = sdirectory.getContext();

        if (item == view) {
            FileIntent.view(c, new Intent(), sdirectory.getValueAsFile());

        } else if (item == get) {
            FileIntent.browse(c, new Intent(),
                    Uri.parse(sdirectory.getValueAsFile().toString()));

        } else if (item == clipboard) {
            new Clipboard(c).setText(sdirectory.getLabel(),
                    sdirectory.getValueAsString());

        } else if (item == permission) {
            new AndroidVolumes(sdirectory.getContext()).
                    askForPermission(acontext, sdirectory.getValueAsFile());

            Foc content = new AndroidVolumes(c).toScopedContentHack(c, sdirectory.getValueAsFile());
            AppLog.d(this, content.toString());


            sdirectory.setValue(content.toString());

        } else {
            return false;
        }
        return true;
    }

}
