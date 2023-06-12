package ch.bailu.aat.views;

import android.content.Context;

import ch.bailu.aat.views.html.HtmlTextView;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class StatusTextView extends VerticalScrollView {
    public StatusTextView(Context context) {
        super(context);

        HtmlTextView textView = new HtmlTextView(context);
        add(textView);
    }
}
