package ch.bailu.aat.views.html;

import android.content.Context;
import android.text.util.Linkify;

import ch.bailu.aat.views.preferences.VerticalScrollView;


public class HtmlScrollTextView extends VerticalScrollView {
    private final HtmlTextView textView;

    public HtmlScrollTextView(Context context) {
        super(context);

        textView = new HtmlTextView(context);
        add(textView);
    }



    public HtmlScrollTextView(Context context, String text, LinkHandler linkHandler) {
        this(context);
        setHtmlText(text);
        textView.setLinkHandler(linkHandler);
    }


    public HtmlScrollTextView(Context context, String text) {
        this(context);
        setHtmlText(text);
    }


    public void setHtmlText(String text) {

        textView.setHtmlText(text);
    }


    public void enableAutoLink() {
        textView.setAutoLinkMask(Linkify.ALL);
    }

    public HtmlTextView getTextView() {
        return textView;
    }
}
