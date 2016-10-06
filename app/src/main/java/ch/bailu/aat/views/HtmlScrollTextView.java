package ch.bailu.aat.views;

import android.content.Context;

import ch.bailu.aat.views.preferences.VerticalScrollView;


public class HtmlScrollTextView extends VerticalScrollView {
    private final HtmlTextView textView;

    public HtmlScrollTextView(Context context) {
        super(context);

        textView = new HtmlTextView(context);
        add(textView);
    }


    public HtmlScrollTextView(Context context, int res) {
        this(context, context.getString(res));
    }


    public HtmlScrollTextView(Context context, String text) {
        this(context);
        setHtmlText(text);
    }


    public void setHtmlText(String text) {
        textView.setHtmlText(text);
    }
}
