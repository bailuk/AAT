package ch.bailu.aat.views.msg.overlay;

import android.content.Context;

import ch.bailu.aat.util.ui.tooltip.ToolTip;
import ch.bailu.aat.views.msg.AbsMsgView;

public class TipMsgView extends AbsMsgView {
    private final static int DISPLAY_FOR_MILLIS = 6000;

    public TipMsgView(Context context) {
        super(context, DISPLAY_FOR_MILLIS);
        ToolTip.themeify(this);
    }

    @Override
    public void attach() {}

    @Override
    public void detach() {}
}
