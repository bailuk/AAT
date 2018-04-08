package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import ch.bailu.aat.services.background.Downloads;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.net.URX;

public class DownloadMessageView extends MessageView {

    public DownloadMessageView(Context context) {
        super(context, AppBroadcaster.ON_DOWNLOADS_CHANGED);
        setTextColor(Color.WHITE);
        setSingleLine();
    }


    public void updateContent(Intent intent) {

    }

    @Override
    public void updateContent() {
        URX x = Downloads.getSource();

        if (x != null) {
            setText(x.toString());
            setTextColor(Color.WHITE);
            enableText();
        } else {
            setTextColor(Color.LTGRAY);
            disableText();
        }
    }
}
