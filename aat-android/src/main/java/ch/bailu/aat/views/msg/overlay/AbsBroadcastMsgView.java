package ch.bailu.aat.views.msg.overlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.views.msg.AbsMsgView;

public abstract class AbsBroadcastMsgView extends AbsMsgView {

    private final static int DISPLAY_FOR_MILLIS = 6000;
    private final String broadcastMessage;

    private final BroadcastReceiver onMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AbsBroadcastMsgView.this.set(intent);
        }
    };

    AbsBroadcastMsgView(Context context, String bMsg) {
        super(context, DISPLAY_FOR_MILLIS);
        broadcastMessage = bMsg;
    }

    @Override
    public void attach() {
        OldAppBroadcaster.register(getContext(), onMessage, broadcastMessage);
    }

    @Override
    public void detach() {
        getContext().unregisterReceiver(onMessage);
    }

    public abstract void set(Intent intent);
}
