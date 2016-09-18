package ch.bailu.aat.helpers;

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
        if (android.os.Build.VERSION.SDK_INT < 11) {
            setTextSDK1(text);
        } else {
            setTextSDK11(label, text);
        }
    }



    public CharSequence getText() {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            return getTextSDK1();
        } else {
            return getTextSDK11();
        }
    }


    @SuppressWarnings("deprecation")
    private void setTextSDK1(CharSequence text) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }


    @SuppressWarnings("deprecation")
    private CharSequence getTextSDK1() {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboard.getText().toString();
    }


    @TargetApi(11)
    private void setTextSDK11(CharSequence label, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    @TargetApi(11)
    private CharSequence getTextSDK11() {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            ClipData.Item i = clipboard.getPrimaryClip().getItemAt(0);
            if (i!= null) {
                return i.getText();
            }
        }
        return null;
    }
}
