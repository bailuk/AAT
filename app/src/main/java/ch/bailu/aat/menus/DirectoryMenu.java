package ch.bailu.aat.menus;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.Clipboard;
import ch.bailu.aat.util.fs.FileIntent;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;

public class DirectoryMenu extends AbsMenu {
    private static final int BROWSE_DIR = DirectoryMenu.class.getSimpleName().hashCode();
    public static final int PERMISSION = DirectoryMenu.class.getSimpleName().hashCode();
    private static String browseDirKey=null;

    private MenuItem browse, view, get, clipboard, permission;

    private final Activity acontext;

    private final SolidFile sdirectory;
    public DirectoryMenu(Activity c, SolidFile d) {
        acontext = c;
        sdirectory = d;
    }


    @Override
    public void inflate(Menu menu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            browse = menu.add("Pick directory...*");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            permission = menu.add("Permission");
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
/*
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
*/
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

        } else if (item == permission) {
            new AndroidVolumes(acontext).askForPermission(acontext, sdirectory.getValueAsFile());
        }

        return true;
    }

    public static void onActivityResult(Activity c, int requestCode, int resultCode, Intent data) {
        if (requestCode == DirectoryMenu.BROWSE_DIR) {
            if (resultCode == Activity.RESULT_OK && data != null && DirectoryMenu.browseDirKey != null) {
                String value = data.getData().toString();
                Storage.global(c).writeString(browseDirKey, value);
                browseDirKey = null;
                test(c, data.getData());
            }
        } else if (requestCode == PERMISSION) {
            test(c, data.getData());

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void test(Activity acontext, Uri uri) {
/*
        String file = DocumentsContract.getTreeDocumentId(uri) + "/Arbeit.gpx";
        Uri doc = DocumentsContract.buildDocumentUriUsingTree(uri, file);

        AppLog.d(acontext, file);
        AppLog.d(acontext, doc.toString());
        try {
            Closeable close = acontext.getContentResolver().openInputStream(doc);

            if (close != null) {
                AppLog.d(acontext, "success!!!");
                close.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        Foc file = FocAndroid.factory(acontext, uri);

        rlog(file);
    }

    private static boolean write(Foc file) {
        Closeable c=null;
        try {
            c = file.openR();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            Foc.close(c);
        }
    }

    private static boolean read(Foc file) {
        Closeable c=null;
        try {
            c = file.openW();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            Foc.close(c);
        }
    }

    private static void rlog(Foc file) {
        log(file);
        file.foreach(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                rlog(child);
            }
        });

    }

    private static void log(Foc file) {
        AppLog.d(file, "====log()==================");
        AppLog.d(file, file.toString());
        AppLog.d(file, file.getName());
        AppLog.d(file, "Size: " + file.length());

        if (file.isDir())
            AppLog.d(file, "isDirectory");

        if (file.isFile())
            AppLog.d(file, "isFile");

        if (file.canWrite())
            AppLog.d(file, "canWrite");
        if (file.canRead())
            AppLog.d(file, "canRead");

        if (file.exists())
            AppLog.d(file, "exists");

        if (read(file))
            AppLog.d(file, "opensForReading");

        if (write(file)){
            AppLog.d(file, "opensForWriting");
        }
        AppLog.d(file, "===========================");
    }


}


