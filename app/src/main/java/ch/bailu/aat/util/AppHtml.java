package ch.bailu.aat.util;

import android.text.Html;
import android.text.Spanned;

public class AppHtml {
    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(source,Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            return Html.fromHtml(source);
        }
    }

    public static Spanned fromHtml(String source, Html.ImageGetter imageGetter) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(source,Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            //noinspection deprecation
            return Html.fromHtml(source, imageGetter, null);
        }

    }
}
