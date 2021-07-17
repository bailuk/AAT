package ch.bailu.aat.views.msg_overlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.util.OldAppBroadcaster;

public abstract class AbsBroadcastMsgView extends AbsMsgView {

    private final String broadcastMessage;

    private final BroadcastReceiver onMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AbsBroadcastMsgView.this.set(intent);
        }
    };


    AbsBroadcastMsgView(Context context, String bMsg) {
        super(context);
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
