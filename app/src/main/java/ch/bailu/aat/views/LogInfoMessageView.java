package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;

public class LogInfoMessageView extends MessageView {
    public LogInfoMessageView(Context context) {
        super(context, AppLog.LOG_INFO);

        setBackgroundColor(MapColor.LIGHT);
        setTextColor(Color.BLACK);
        AppTheme.padding(this,10);
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
