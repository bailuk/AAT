package ch.bailu.aat.menus;


import android.graphics.drawable.Drawable;
import android.view.Menu;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.util.fs.AndroidFileAction;
import ch.bailu.foc.Foc;

public final class ContentMenu extends AbsMenu {
    private final Foc uri;
    private final ActivityContext context;

    public ContentMenu(ActivityContext context, Foc uri) {
        this.uri = uri;
        this.context = context;
    }

    @Override
    public void inflate(Menu menu) {
        add(menu, R.string.file_send, ()-> AndroidFileAction.sendTo(context, uri));
        add(menu, R.string.file_view, ()-> AndroidFileAction.view(context, uri));
        add(menu, R.string.file_copy, ()-> AndroidFileAction.copyToDir(context, context.getAppContext(), uri));
        add(menu, R.string.clipboard_copy, ()-> AndroidFileAction.copyToClipboard(context, uri));
    }

    @Override
    public String getTitle() {
        return uri.getName();
    }

    @Override
    public Drawable getIcon() {
        return null;
    }


    @Override
    public void prepare(Menu menu) {

    }
}
