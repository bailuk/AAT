package ch.bailu.aat.views.msg_overlay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;

public class DownloadMsgView extends AbsBroadcastMsgView {

    public DownloadMsgView(Context context) {
        super(context, AppBroadcaster.FILE_CHANGED_ONDISK);
        setTextColor(Color.WHITE);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MIDDLE);
    }

    @Override
    public void set(Intent intent) {
        String url = AppIntent.getUrl(intent);

        if (url != null && url.startsWith("http")) {
            set(url);
        }
    }
}
