package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import ch.bailu.aat.helpers.AppHtml;
import ch.bailu.aat.helpers.AppTheme;


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

}
