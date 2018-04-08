package ch.bailu.aat.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ContentView extends FrameLayout{
    private final LinearLayout mainContent;
    private final LinearLayout messages;

    private final ArrayList<MessageView> messageViews = new ArrayList(5);

    public ContentView(Context context) {
        super(context);
        mainContent = new LinearLayout(context);
        mainContent.setOrientation(LinearLayout.VERTICAL);
        addView(mainContent);

        messages = new LinearLayout(context);
        messages.setOrientation(LinearLayout.VERTICAL);
        addView(messages);

        addM(new DownloadMessageView(context));
        addM(new FileMessageView(context));
        addM(new LogInfoMessageView(context));
    }


    private void addM(MessageView v) {
        messages.addView(v);
        messageViews.add(v);
    }

    public void add(View view) {
        mainContent.addView(view);
        messages.bringToFront();
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (MessageView v: messageViews) {
            v.attach();
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
