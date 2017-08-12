package ch.bailu.aat.menus;


import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.util_java.foc.Foc;

public class ContentMenu extends AbsMenu {
    private final Foc uri;
    private final ServiceContext scontext;

    private MenuItem send, view, copy, clipboard;

    public ContentMenu(ServiceContext sc, Foc u) {
        uri = u;
        scontext = sc;
    }

    @Override
    public void inflate(Menu menu) {
        send = menu.add(R.string.file_send);
        view = menu.add(R.string.file_view);
        copy = menu.add(R.string.file_copy);
        clipboard = menu.add(R.string.clipboard_copy);
    }

    @Override
    public void inflateWithHeader(ContextMenu menu) {
        menu.setHeaderTitle(uri.getName());
        inflate(menu);
    }

    @Override
    public void prepare(Menu menu) {

    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == send) {
            FileAction.sendTo(scontext.getContext(), uri);

        } else if (item == view) {
            FileAction.view(scontext.getContext(), uri);

        } else if (item == copy) {
            FileAction.copyToDir(scontext.getContext(), uri);

        } else if (item == clipboard) {
            FileAction.copyToClipboard(scontext.getContext(), uri);
        } else  {
            return false;
        }
        return true;
    }
}
