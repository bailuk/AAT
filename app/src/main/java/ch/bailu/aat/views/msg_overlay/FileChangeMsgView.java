package ch.bailu.aat.views.msg_overlay;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

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
