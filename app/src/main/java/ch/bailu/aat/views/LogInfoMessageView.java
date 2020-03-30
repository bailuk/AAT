package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import ch.bailu.aat.util.ui.AppLog;

public class LogInfoMessageView extends MessageView {
    public LogInfoMessageView(Context context) {
        super(context, AppLog.LOG_INFO);

        setTextColor(Color.WHITE);
        setTypeface(null, Typeface.BOLD);
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
