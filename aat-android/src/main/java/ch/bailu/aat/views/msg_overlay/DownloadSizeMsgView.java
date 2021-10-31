package ch.bailu.aat.views.msg_overlay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import ch.bailu.aat.services.background.DownloaderThread;
import ch.bailu.aat_lib.util.MemSize;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public class DownloadSizeMsgView extends AbsBroadcastMsgView {
    private long size = 0;
    final StringBuilder builder = new StringBuilder();


    public DownloadSizeMsgView(Context context) {
        super(context, AppBroadcaster.FILE_CHANGED_ONDISK);
        setTextColor(Color.WHITE);
    }

    @Override
    public void set(Intent intent) {
        long newSize = DownloaderThread.getTotalSize();

        if (size != newSize) {
            size = newSize;
            builder.setLength(0);
            MemSize.describe(builder, (double)size);

            set(builder.toString());
        }
    }
}
