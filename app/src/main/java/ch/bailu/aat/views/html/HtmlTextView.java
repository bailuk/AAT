package ch.bailu.aat.views.html;

import android.content.Context;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import ch.bailu.aat.util.AppHtml;


public class HtmlTextView extends TextView {

    public HtmlTextView(Context context) {
        super(context);

        int p = Math.round(getTextSize());

        setPadding(p,p,p,p);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setHtmlText(String text) {
        setText(AppHtml.fromHtml(text));
    }

    public void setLinkHandler(LinkHandler linkHandler) {
        setText(linkHandler.convertAll((Spanned)getText()));
    }

    public void enableAutoLink() {
        setAutoLinkMask(Linkify.ALL);
    }
}
