package ch.bailu.aat.preferences;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.DirectoryMenu;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;


public abstract class SolidFile extends SolidString {



    public SolidFile(Context c, String k) {
        super(c, k);
    }


    public Foc getValueAsFile() {
        return FocAndroid.factory(getContext(), getValueAsString());
    }




    @NonNull
    @Override
    public String toString() {
        return getValueAsFile().getPathName();
    }



    public int getIconResource() {return R.drawable.folder_inverse;}

    public abstract ArrayList<String> buildSelection(ArrayList<String> list);


    public static void add_ro(ArrayList<String> list, Foc file) {
        add_ro(list, file, file);
    }

    public static void add_ro(ArrayList<String> list, Foc check, Foc file) {
        if (check.canOnlyRead()) {
            list.add(file.toString());
        }
    }

    public static void add_r(ArrayList<String> list, Foc file) {
        if (file.canRead())
            list.add(file.toString());
    }

    public static void add_subdirectories_r(final ArrayList<String> list, Foc directory) {
        directory.foreachDir(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                add_r(list, child);
            }
        });
    }


    public static void add_w(ArrayList<String> list, Foc file) {
        add_w(list, file, file);

    }


    public static void add_w(ArrayList<String> list, Foc check, Foc file) {
        if (file != null && check.canWrite())  {
            list.add(file.toString());
        }
    }



    private static final int BROWSE_DIR = DirectoryMenu.class.getSimpleName().hashCode();
    private static String browseDirKey=null;


    public static void onActivityResult(Activity c, int requestCode, int resultCode, Intent data) {

        if (requestCode == BROWSE_DIR) {
            if (resultCode == Activity.RESULT_OK
                    && data != null
                    && browseDirKey != null) {

                Uri uri = data.getData();

                if (uri != null) {

                    requestPersistablePermission(c, uri);

                    Storage storage = new Storage(c);
                    storage.writeString(browseDirKey, ""); // forces update notification
                    // (needed because permission changed)
                    storage.writeString(browseDirKey, uri.toString());
                }
            }
            browseDirKey = null;
        }
    }


    public static void requestPersistablePermission(Activity a, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            a.getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }


    @TargetApi(FocAndroid.SAF_MIN_SDK)
    public void setFromPickerActivity(Activity acontext) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        browseDirKey = getKey();

        try  {
            acontext.startActivityForResult(intent, BROWSE_DIR);
        } catch (Exception e) {
            browseDirKey = null;
            e.printStackTrace();
        }
    }


    @Override
    public String getToolTip() {
        return  getPermissionText(getContext(), getValueAsFile());
    }

    private static String getPermissionText(Context c, Foc f) {

        if (f.exists() == false) {
            if (f.hasParent()) {
                return getPermissionText(c, f.parent());
            } else {
                return f.getPathName() + c.getString(R.string.file_is_missing);
            }
        } else if (f.canWrite()) {
            if (f.canRead()) {
                return f.getPathName() + c.getString(R.string.file_is_writeable);
            } else {
                return f.getPathName() + c.getString(R.string.file_is_writeonly);
            }
        } else if (f.canRead()) {
            return f.getPathName() + c.getString(R.string.file_is_readonly);
        } else if (f.hasParent()){
            return getPermissionText(c, f.parent());
        } else  {
            return f.getPathName() + c.getString(R.string.file_is_denied);
        }
    }
}
