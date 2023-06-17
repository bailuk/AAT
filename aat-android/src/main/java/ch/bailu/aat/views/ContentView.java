package ch.bailu.aat.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import ch.bailu.aat.app.ActivitySwitcher;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages;
import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.description.mview.MultiViewIndicator;
import ch.bailu.aat.views.msg.AbsMsgView;
import ch.bailu.aat.views.msg.overlay.DownloadMsgView;
import ch.bailu.aat.views.msg.overlay.DownloadSizeMsgView;
import ch.bailu.aat.views.msg.overlay.FileChangeMsgView;
import ch.bailu.aat.views.msg.overlay.InfoLogMsgView;
import ch.bailu.aat.views.msg.overlay.MessageOverlay;
import ch.bailu.aat.views.msg.overlay.TipMsgView;

public class ContentView extends FrameLayout{
    private final LinearLayout mainContent;
    private final MessageOverlay messages;

    private final AbsMsgView ttBottom;
    private final AbsMsgView ttTop;


    public ContentView(Context context, UiTheme theme) {
        super(context);

        theme.background(this);

        mainContent = new LinearLayout(context);
        mainContent.setOrientation(LinearLayout.VERTICAL);
        addView(mainContent);

        messages = new MessageOverlay(context);
        addView(messages);

        SolidStatusMessages smessages = new SolidStatusMessages(new Storage(context));


        if (smessages.showURL()) {
            messages.add(new DownloadMsgView(context));
        }

        if (smessages.showPath()) {
            messages.add(new FileChangeMsgView(context));
        }

        if (smessages.showSummary()) {
            messages.add(new DownloadSizeMsgView(context));
        }

        ttTop = messages.add(new InfoLogMsgView(context));
        messages.addSpace();
        ttBottom = messages.addR(new TipMsgView(context));

        logActivityLabel();
    }

    private void logActivityLabel() {
        final ActivitySwitcher.Entry e = ActivitySwitcher.get(getContext());

        if (e != null) {
            ttTop.set(e.getActivityLabel());
        }
    }

    public void add(View view) {
        mainContent.addView(view);
        messages.bringToFront();
    }

    public void addW(View v) {
        add(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }

    public void addMvIndicator(MultiView mv) {
        messages.addView(new MultiViewIndicator(mv), 0);
    }

    public void showTip(String string) {
        ttBottom.set(string);
    }
}
