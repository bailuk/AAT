package ch.bailu.aat.menus;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.util_java.foc.Foc;

public class FileMenu extends AbsMenu {
    private final Foc file;

    private final String targetPrefix, targetExtendsion;

    private final Activity activity;
    private final ServiceContext scontext;

    private MenuItem send, view, rename, copy, delete, overlay, reload, mock, clipboard;


    public FileMenu(AbsServiceLink a, Foc f) {
        this(a, f, null, null);
    }


    public FileMenu(AbsServiceLink a, Foc f, String prefix, String extension) {
        targetExtendsion = extension;
        targetPrefix = prefix;
        file = f;
        scontext = a.getServiceContext();
        activity = a;
    }




    @Override
    public void inflate(Menu menu) {
        overlay = menu.add(R.string.file_overlay);

        copy = menu.add(R.string.file_copy);

        clipboard = menu.add(R.string.clipboard_copy);
        send = menu.add(R.string.file_send);

        if (targetPrefix == null) {
            view = menu.add(R.string.file_view);
            rename = menu.add(R.string.file_rename);
            delete = menu.add(R.string.file_delete);
            reload = menu.add(R.string.file_reload);
            mock = menu.add(R.string.file_mock);
        }
    }

    @Override
    public String getTitle() {
        return file.getName();
    }

    @Override
    public Drawable getIcon() {
        return null;
    }


    @Override
    public void prepare(Menu menu) {

    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == delete) {
            FileAction.delete(scontext, activity, file);

        } else if (item == reload) {
            FileAction.reloadPreview(scontext, file);

        } else if (item == rename) {
            FileAction.rename(scontext, activity, file);

        } else if (item == overlay) {
            FileAction.useAsOverlay(scontext.getContext(), file);


        } else if (item == mock) {
            FileAction.useForMockLocation(scontext.getContext(), file);

        } else if (item == send) {
            FileAction.sendTo(scontext.getContext(), file);

        } else if (item == view) {
            FileAction.view(scontext.getContext(), file);

        } else if (item == copy) {
            if (targetPrefix == null)
                FileAction.copyToDir(scontext.getContext(), file);
            else
                FileAction.copyToDir(scontext.getContext(), file, targetPrefix, targetExtendsion);

        } else if (item == clipboard) {
            FileAction.copyToClipboard(scontext.getContext(), file);
        } else  {
            return false;
        }
        return true;
    }

}
