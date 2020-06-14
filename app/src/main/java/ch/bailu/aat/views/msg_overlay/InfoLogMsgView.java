package ch.bailu.aat.views.msg_overlay;

import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.ToolTip;

public class InfoLogMsgView extends AbsBroadcastMsgView {
    public InfoLogMsgView(Context context) {
        super(context, AppLog.LOG_INFO);
        ToolTip.themeify(this);
    }

    @Override
    public void set(Intent intent) {
        String message = intent.getStringExtra(AppLog.EXTRA_MESSAGE);
        set(message);
    }
}
