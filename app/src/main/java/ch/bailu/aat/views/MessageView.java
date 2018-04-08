package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.widget.TextView;

import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ui.AppLayout;

public abstract class MessageView extends TextView implements Runnable{

    private final static int TIMEOUT=4000;

    private final Timer eraseText = new Timer(this, TIMEOUT);
    private final String msg;

    private final BroadcastReceiver onChagned = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateContent(intent);
        }
    };


    public MessageView(Context context, String m) {
        super(context);
        msg = m;

        setBackgroundColor(MapColor.DARK);
        setVisibility(GONE);
    }




    public void attach() {
        AppBroadcaster.register(getContext(), onChagned,
                msg);

        updateContent(null);
    }


    public void detach() {
        getContext().unregisterReceiver(onChagned);

        super.onDetachedFromWindow();
    }

    public abstract void updateContent(Intent intent);

    public void enableText() {
        if (getVisibility() == GONE)
            AppLayout.fadeIn(this);

        eraseText.cancel();
    }

    public void disableText() {
        eraseText.kick();
    }


    @Override
    public void run() {
        if (getVisibility() == VISIBLE)
            AppLayout.fadeOut(this);

        setText("");
    }
}
