package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import ch.bailu.aat.services.background.Downloads;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.net.URX;
import ch.bailu.util_java.foc.Foc;

public class DownloadingMessageView extends MessageView {

    public DownloadingMessageView(Context context) {
        super(context, AppBroadcaster.ON_DOWNLOADS_CHANGED);
        setTextColor(Color.WHITE);
        setSingleLine();
    }



    public void updateContent(Intent intent) {
        URX x = Downloads.getSource();
        Foc f = Downloads.getFile();

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
