package ch.bailu.aat.menus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.fs.FileIntent;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;

public class DirectoryMenu extends AbsMenu {
    private static final int BROWSE_DIR = DirectoryMenu.class.getSimpleName().hashCode();
    private static String browseDirKey=null;

    private MenuItem browse, view, get, clipboard;

    private final Activity acontext;

    private final SolidFile sdirectory;
    public DirectoryMenu(Activity c, SolidFile d) {
        acontext = c;
        sdirectory = d;
    }


    @Override
    public void inflate(Menu menu) {
        if (Build.VERSION.SDK_INT >= 21) {
            browse = menu.add("Pick directory...*");
        }

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
        Context c = sdirectory.getContext();

        if (item == browse) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            browseDirKey = sdirectory.getKey();
            acontext.startActivityForResult(intent, BROWSE_DIR);

        } else if  (item == view) {
            FileIntent.view(c, new Intent(), sdirectory.getValueAsFile());

        } else if (item == get) {
            FileIntent.browse(c, new Intent(),
                    Uri.parse(sdirectory.getValueAsFile().toString()));

        } else if (item == clipboard) {
            new Clipboard(c).setText(sdirectory.getLabel(),
                    sdirectory.getValueAsString());

        }
        return true;
    }

    public static void onActivityResult(Context c, int requestCode, int resultCode, Intent data) {
        if (requestCode == DirectoryMenu.BROWSE_DIR) {
            if (resultCode == Activity.RESULT_OK && data != null && DirectoryMenu.browseDirKey != null) {
                String value = data.getData().toString();
                Storage.global(c).writeString(browseDirKey, value);
                browseDirKey = null;
            }
        }
    }
}
