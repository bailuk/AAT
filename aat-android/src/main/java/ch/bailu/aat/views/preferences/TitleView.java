package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.widget.TextView;

import ch.bailu.aat.util.ui.theme.UiTheme;

public class TitleView extends TextView {
    public TitleView(Context context, int res, UiTheme theme) {
        this(context, context.getText(res), theme);
    }


    public TitleView(Context context, CharSequence text, UiTheme theme) {
        super(context);

        setText(text);
        theme.topic(this);
    }
}
