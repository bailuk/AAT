package ch.bailu.aat.views.msg_overlay;

import android.content.Context;
import android.widget.TextView;

import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat.util.AndroidTimer;
import ch.bailu.aat.util.ui.AppLayout;

public abstract class AbsMsgView extends TextView {

    private final static int SHOW_TEXT_MILLIS = 4000;

    private final AndroidTimer fadeOutTimer = new AndroidTimer();

    AbsMsgView(Context context) {
        super(context);
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

        fadeOutTimer.kick(()->set(), SHOW_TEXT_MILLIS);
    }



    public void set() {
        if (getVisibility() == VISIBLE) {
            AppLayout.fadeOut(this);
        }
    }
}
