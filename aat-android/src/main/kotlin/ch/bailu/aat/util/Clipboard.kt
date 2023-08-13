package ch.bailu.aat.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class Clipboard(private val context: Context) {
    fun setText(label: CharSequence, text: CharSequence) {
        setTextSDK11(label, text)
    }

    val text: CharSequence
        get() = textSDK11

    private fun setTextSDK11(label: CharSequence, text: CharSequence) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
        if (clipboard is ClipboardManager) {
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
        }
    }

    private val textSDK11: CharSequence
        get() {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
            if (clipboard is ClipboardManager) {
                val clip = clipboard.primaryClip
                if (clip != null) {
                    val i = clipboard.primaryClip!!.getItemAt(0)
                    if (i != null) {
                        return i.text
                    }
                }
            }
            return ""
        }
}
