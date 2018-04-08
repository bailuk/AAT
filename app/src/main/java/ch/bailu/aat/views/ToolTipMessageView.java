package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;

public class ToolTipMessageView extends MessageView {
    public ToolTipMessageView(Context context) {
        super(context, AppLog.LogInfo.ACTION);

        setTextColor(AppTheme.getHighlightColor3());
        setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public void updateContent(Intent intent) {
        if (intent != null) {
            String message = intent.getStringExtra(AppLog.EXTRA_MESSAGE);
            setText(message);
            enableText();
        }
        disableText();
    }
}
