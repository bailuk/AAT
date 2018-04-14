package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ui.AppLayout;

public abstract class MessageView extends TextView implements Runnable{

    private final static int SHOW_TEXT_MILLIS = 4000;

    private final Timer fadeOutTimer = new Timer(this, SHOW_TEXT_MILLIS);
    private final String broadcastMessage;

    private final BroadcastReceiver onMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateContent();
            updateContent(intent);
        }
    };


    MessageView(Context context, String bMsg) {
        super(context);
        broadcastMessage = bMsg;

        setBackgroundColor(MapColor.DARK);
        setVisibility(GONE);

    }




    public void attach() {
        AppBroadcaster.register(getContext(), onMessage, broadcastMessage);
        updateContent();
    }


    public void detach() {
        getContext().unregisterReceiver(onMessage);
    }

    protected abstract void updateContent(Intent intent);
    protected abstract void updateContent();

    void enableText() {
        if (getVisibility() == GONE)
            AppLayout.fadeIn(this);

        fadeOutTimer.cancel();
    }

    void disableText() {
        fadeOutTimer.kick();
    }


    @Override
    public void run() {
        if (getVisibility() == VISIBLE)
            AppLayout.fadeOut(this);

        setText("");
    }
}
