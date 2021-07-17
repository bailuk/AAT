package ch.bailu.aat.views.msg_overlay;

import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public class InfoLogMsgView extends AbsBroadcastMsgView {
    public InfoLogMsgView(Context context) {
        super(context, AppBroadcaster.LOG_INFO);
        ToolTip.themeify(this);
    }

    @Override
    public void set(Intent intent) {
        String message = intent.getStringExtra(AppBroadcaster.EXTRA_MESSAGE);
        set(message);
    }
}
