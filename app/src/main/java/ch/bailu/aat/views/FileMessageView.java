package ch.bailu.aat.views;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

public class FileMessageView  extends MessageView{
    public FileMessageView(Context context) {
        super(context, AppBroadcaster.FILE_CHANGED_ONDISK);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MIDDLE);
    }

    @Override
    public void updateContent(Intent intent) {
        Foc file = FocAndroid.factory(getContext(), AppIntent.getFile(intent));
        setText(file.getPathName());
        enableText();
        disableText();
    }

    @Override
    public void updateContent() {

    }
}
