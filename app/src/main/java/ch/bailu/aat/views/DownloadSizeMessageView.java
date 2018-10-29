package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import ch.bailu.aat.services.background.DownloaderThread;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.MemSize;

public class DownloadSizeMessageView extends MessageView {
    private long size = 0;
    StringBuilder builder = new StringBuilder();


    DownloadSizeMessageView(Context context) {
        super(context, AppBroadcaster.FILE_CHANGED_ONDISK);
        setTextColor(Color.WHITE);
    }

    @Override
    protected void updateContent() {

    }

    @Override
    protected void updateContent(Intent intent) {
        long newSize = DownloaderThread.getTotalSize();

        if (size != newSize) {
            size = newSize;
            builder.setLength(0);
            MemSize.describe(builder, (double)size);

            setText(builder.toString());
            enableText();
            disableText();
        }
    }
}
