package ch.bailu.aat.preferences

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import ch.bailu.aat_lib.preferences.SolidFile

/**
 * Helper that handles SAF access permission requests from the user.
 */
class SolidSAF(private val sdirectory: SolidFile) {
    /**
     * Opens Androids file selection activity so that the user can select a directory.
     * Stores the solid key of the SolidFile object to later handle
     * the result of this permission request.
     * @param acontext The calling activity
     */
    fun setFromPickerActivity(acontext: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        browseDirKey = sdirectory.key
        try {
            acontext.startActivityForResult(intent, BROWSE_DIR)
        } catch (e: Exception) {
            browseDirKey = ""
            e.printStackTrace()
        }
    }

    companion object {
        private val BROWSE_DIR = SolidSAF::class.java.simpleName.hashCode()
        private var browseDirKey: String = ""

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
        fun onActivityResult(c: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == BROWSE_DIR) {
                if (resultCode == Activity.RESULT_OK && data != null && browseDirKey.isNotEmpty()) {
                    val uri = data.data
                    if (uri != null) {
                        requestPersistablePermission(c, uri)
                        forceSolidUpdateNotification(c, uri)
                    }
                }
                browseDirKey = ""
            }
        }

        private fun requestPersistablePermission(a: Activity, uri: Uri) {
            a.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }

        private fun forceSolidUpdateNotification(c: Context, uri: Uri) {
            if (browseDirKey.isNotEmpty()) {
                val storage = Storage(c)
                storage.writeString(browseDirKey, "")
                storage.writeString(browseDirKey, uri.toString())
            }
        }
    }
}
