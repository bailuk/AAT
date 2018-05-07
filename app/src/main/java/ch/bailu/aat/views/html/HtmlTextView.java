package ch.bailu.aat.views.html;

import android.content.Context;
import android.graphics.Color;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import ch.bailu.aat.util.AppHtml;
import ch.bailu.aat.util.ui.AppTheme;


public class HtmlTextView extends TextView {
    public HtmlTextView(Context context) {
        super(context);

        new TextView(context);
        setTextSize(15f);
        setMovementMethod(LinkMovementMethod.getInstance());
        setLinkTextColor(AppTheme.getHighlightColor());
        setTextColor(Color.LTGRAY);

    }

    public void setHtmlText(String text) {
        setText(AppHtml.fromHtml(text));
    }

    public void setLinkHandler(LinkHandler linkHandler) {
        setText(linkHandler.convertAll((Spanned)getText()));
    }

}
