package ch.bailu.aat.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.preferences.system.SolidStatusMessages;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;

public class ContentView extends FrameLayout{
    private final LinearLayout mainContent;
    private final LinearLayout messages;

    private final ArrayList<MessageView> messageViews = new ArrayList(5);


    public ContentView(Context context) {
        super(context);

        AppTheme.main.background(this);

        mainContent = new LinearLayout(context);
        mainContent.setOrientation(LinearLayout.VERTICAL);
        addView(mainContent);

        messages = new LinearLayout(context);
        messages.setOrientation(LinearLayout.VERTICAL);
        addView(messages);

        SolidStatusMessages smessages = new SolidStatusMessages(context);


        if (smessages.showURL()) {
            addM(new DownloadMessageView(context));
        }

        if (smessages.showPath()) {
            addM(new FileMessageView(context));
        }

        addM(new LogInfoMessageView(context));

        if (smessages.showSummary()) {
            addM(new DownloadSizeMessageView(context));
        }
    }


    private void addM(MessageView v) {
        messages.addView(v,
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        messageViews.add(v);
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

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (MessageView v: messageViews) {
            v.attach();
        }


        logActivityLabel();
    }


    private void logActivityLabel() {
        final ActivitySwitcher.Entry e = ActivitySwitcher.get(getContext());

        if (e != null) {
            AppLog.i(getContext(), e.activityLabel);
        }
    }


    @Override
    public void onDetachedFromWindow() {
        for (MessageView v: messageViews) {
            v.detach();
        }

        super.onDetachedFromWindow();
    }
}
