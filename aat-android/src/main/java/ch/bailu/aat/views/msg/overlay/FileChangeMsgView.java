package ch.bailu.aat.views.msg.overlay;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public class FileChangeMsgView extends AbsBroadcastMsgView {
    public FileChangeMsgView(Context context) {
        super(context, AppBroadcaster.FILE_CHANGED_ONDISK);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MIDDLE);
    }

    @Override
    public void set(Intent intent) {
        Foc file = FocAndroid.factory(getContext(), AppIntent.getFile(intent));
        set(file.getPathName());
    }
}
