package ch.bailu.aat.util;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class Clipboard {
    private final Context context;


    public Clipboard(Context c) {
        context = c;
    }


    public void setText(CharSequence label, CharSequence text) {
        setTextSDK11(label, text);
    }



    public CharSequence getText() {
        return getTextSDK11();
    }


    @TargetApi(11)
    private void setTextSDK11(CharSequence label, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
        }
    }

    @TargetApi(11)
    private CharSequence getTextSDK11() {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = clipboard.getPrimaryClip();
            if (clip != null) {
                ClipData.Item i = clipboard.getPrimaryClip().getItemAt(0);
                if (i != null) {
                    return i.getText();
                }
            }
        }
        return null;
    }
}
