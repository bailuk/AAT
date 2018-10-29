package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;

public class DownloadMessageView extends MessageView {

    public DownloadMessageView(Context context) {
        super(context, AppBroadcaster.FILE_CHANGED_ONDISK);
        setTextColor(Color.WHITE);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MIDDLE);
    }


    @Override
    public void updateContent() {

    }

    @Override
    public void updateContent(Intent intent) {
        String url = AppIntent.getUrl(intent);

        if (url != null && url.startsWith("http")) {
            setText(url);
            enableText();
            disableText();
        }
        /*
        long size = DownloaderThread.getTotalSize();
        StringBuilder builder = new StringBuilder();
        MemSize.describe(builder, size);
        setText(builder.toString());
        setTextColor(Color.WHITE);
        enableText();
        */
    }
}
