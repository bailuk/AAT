package ch.bailu.aat.views.html;

import android.content.Context;

import ch.bailu.aat.util.ui.UiTheme;
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
        textView.enableAutoLink();
    }

    public HtmlTextView getTextView() {
        return textView;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        textView.setOnClickListener(l);
        super.setOnClickListener(l);
    }

    public void themify(UiTheme theme) {
        theme.content(textView);
    }
}
