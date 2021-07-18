package ch.bailu.aat.preferences;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import ch.bailu.aat.menus.DirectoryMenu;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.foc_android.FocAndroid;

/**
 * Helper that handles SAF access permission requests from the user.
 */
public class SolidSAF {

    private static final int BROWSE_DIR = DirectoryMenu.class.getSimpleName().hashCode();
    private static String browseDirKey=null;

    private final SolidFile sdirectory;


    public SolidSAF(SolidFile s) {
        sdirectory = s;
    }

    /**
     * Opens Androids file selection activity so that the user can select a directory.
     * Stores the solid key of the SolidFile object to later handle
     * the result of this permission request.
     * @param acontext The calling activity
     */
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


    /**
     * Called by the activity that received the permission request result.
     * Receives a full SAF Path of the selected directory including access token.
     * Ask the OS for write permission and then stores the path to the SolidFile objects
     * preferences key. Forces notifying of preferences observers.
     * @param c Calling activity
     * @param requestCode Our identification code, if result is coming from our request.
     * @param resultCode Result status of the permission request.
     * @param data intent with full SAF Path.
     */
    public static void onActivityResult(Activity c, int requestCode, int resultCode, Intent data) {

        if (requestCode == BROWSE_DIR) {
            if (resultCode == Activity.RESULT_OK
                    && data != null
                    && browseDirKey != null) {

                Uri uri = data.getData();

                if (uri != null) {
                    requestPersistablePermission(c, uri);
                    forceSolidUpdateNotification(c, uri);
                }
            }
            browseDirKey = null;
        }
    }

    private static void requestPersistablePermission(Activity a, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            a.getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }


    private static void forceSolidUpdateNotification(Context c, Uri uri) {
        Storage storage = new Storage(c);
        storage.writeString(browseDirKey, "");
        storage.writeString(browseDirKey, uri.toString());

    }
}
