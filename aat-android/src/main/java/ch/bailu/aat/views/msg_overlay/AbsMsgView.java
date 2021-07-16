package ch.bailu.aat.views.msg_overlay;

import android.content.Context;
import android.widget.TextView;

import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ui.AppLayout;

public abstract class AbsMsgView extends TextView {

    private final static int SHOW_TEXT_MILLIS = 4000;

    private final Timer fadeOutTimer = new Timer(
            AbsMsgView.this::set,
            SHOW_TEXT_MILLIS);

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

        fadeOutTimer.kick();
    }



    public void set() {
        if (getVisibility() == VISIBLE) {
            AppLayout.fadeOut(this);
        }
    }
}
