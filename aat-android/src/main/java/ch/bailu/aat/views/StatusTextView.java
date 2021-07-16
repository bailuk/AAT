package ch.bailu.aat.views;

import android.content.Context;

import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.views.html.HtmlTextView;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class StatusTextView extends VerticalScrollView {
    private final HtmlTextView textView;

    public StatusTextView(Context context) {
        super(context);

        textView = new HtmlTextView(context);
        add(textView);
    }


    public void updateText(AbsServiceLink s) {
        StringBuilder content = new StringBuilder();

        s.appendStatusText(content);
        AbsService.appendStatusTextStatic(content);
        s.getServiceContext().appendStatusText(content);

        textView.setHtmlText(content.toString());

    }

}
