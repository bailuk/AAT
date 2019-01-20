package ch.bailu.aat.views.preferences;

import android.content.Context;
import android.widget.TextView;

import ch.bailu.aat.util.ui.AppTheme;

public class TitleView extends TextView {
    public TitleView(Context context, int res) {
        this(context, context.getText(res));
    }


    public TitleView(Context context, CharSequence text) {
        super(context);

        setText(text);
        AppTheme.main.topic(this);
        //setTextColor(AppTheme.getHighlightColor());
    }
}
