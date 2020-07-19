package ch.bailu.aat.views.msg_overlay;

import android.content.Context;

import ch.bailu.aat.util.ui.ToolTip;

public class TipMsgView extends AbsMsgView {
    public TipMsgView(Context context) {
        super(context);
        ToolTip.themeify(this);
    }


    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }
}
