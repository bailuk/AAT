package ch.bailu.aat.menus;


import android.graphics.drawable.Drawable;
import android.view.Menu;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.util_java.foc.Foc;

public final class ContentMenu extends AbsMenu {
    private final Foc uri;
    private final ServiceContext scontext;

    public ContentMenu(ServiceContext sc, Foc u) {
        uri = u;
        scontext = sc;
    }

    @Override
    public void inflate(Menu menu) {
        add(menu, R.string.file_send, ()->FileAction.sendTo(scontext.getContext(), uri));
        add(menu, R.string.file_view, ()->FileAction.view(scontext.getContext(), uri));
        add(menu, R.string.file_copy, ()->FileAction.copyToDir(scontext.getContext(), uri));
        add(menu, R.string.clipboard_copy, ()->FileAction.copyToClipboard(scontext.getContext(), uri));
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
