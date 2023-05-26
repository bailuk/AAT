package ch.bailu.aat.views.msg;

import android.content.Context;
import android.widget.TextView;

import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat.util.AndroidTimer;
import ch.bailu.aat.util.ui.AppLayout;

public abstract class AbsMsgView extends TextView {

    private final AndroidTimer fadeOutTimer = new AndroidTimer();
    private final int displayForMillis;

    public AbsMsgView(Context context, int millis) {
        super(context);
        this.displayForMillis = millis;
        setBackgroundColor(MapColor.DARK);
        setVisibility(GONE);
    }

    public abstract void attach();
    public abstract void detach();

    public void set(String msg) {
        fadeOutTimer.cancel();
        setText(msg);
        if (getVisibility() == GONE)
            AppLayout.fadeIn(this);

        fadeOutTimer.kick(displayForMillis, this::set);
    }

    public void set() {
        if (getVisibility() == VISIBLE) {
            AppLayout.fadeOut(this);
        }
    }
}
