package ch.bailu.aat.preferences;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import ch.bailu.aat.menus.DirectoryMenu;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.foc_android.FocAndroid;

public class SolidSAF {

    private static final int BROWSE_DIR = DirectoryMenu.class.getSimpleName().hashCode();
    private static String browseDirKey=null;

    private SolidFile sdirectory;


    public SolidSAF(SolidFile s) {
        sdirectory = s;
    }


    @TargetApi(FocAndroid.SAF_MIN_SDK)
    public void setFromPickerActivity(Activity acontext) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        browseDirKey = sdirectory.getKey();

        try  {
            acontext.startActivityForResult(intent, BROWSE_DIR);
        } catch (Exception e) {
            browseDirKey = null;
            e.printStackTrace();
        }
    }

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

}
