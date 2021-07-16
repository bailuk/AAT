package ch.bailu.aat.util;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.TypefaceSpan;

import org.xml.sax.XMLReader;

import ch.bailu.aat.util.ui.UiThemeLight;

public class AppHtml {

    private static final int FLAGS =
            Html.FROM_HTML_MODE_LEGACY | Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM;


    public static Spanned fromHtml(String source) {
        return fromHtml(source, null);
    }

    public static Spanned fromHtml(String source, Html.ImageGetter imageGetter) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(source, FLAGS, imageGetter, CODE_TAG_HANDLER);
        } else {
            //noinspection deprecation
            return Html.fromHtml(source, imageGetter, CODE_TAG_HANDLER);
        }
    }


    private static final Html.TagHandler CODE_TAG_HANDLER = new Html.TagHandler() {

        private int code = 0;

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (tag.equals("code")) {
                if (opening) {
                    code = output.length();

                } else {
                    output.setSpan(new BackgroundColorSpan(UiThemeLight.BG_CODE_LIGHT_GRAY),
                            code, output.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    output.setSpan(new TypefaceSpan("monospace"),
                            code,
                            output.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
        }
    };

}
