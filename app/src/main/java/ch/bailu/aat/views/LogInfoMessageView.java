package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;

public class LogInfoMessageView extends MessageView {
    public LogInfoMessageView(Context context) {
        super(context, AppLog.LOG_INFO);


        AppTheme.bar.topic(this);
        AppTheme.bar.toolTip(this);


        //setTextColor(getHighlightColor3());
        //setTypeface(Typeface.MONOSPACE);
        //setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
    }

    @Override
    public void updateContent(Intent intent) {
        String message = intent.getStringExtra(AppLog.EXTRA_MESSAGE);
        setText(message);
        enableText();
        disableText();
    }

    @Override
    public void updateContent() {

    }
}
