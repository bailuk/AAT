package ch.bailu.aat.menus;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.foc.Foc;

public class FileMenu extends AbsMenu {
    protected final Foc file;

    private final Activity activity;
    protected final ServiceContext scontext;

    public FileMenu(AbsServiceLink a, Foc f) {
        file = f;
        scontext = a.getServiceContext();
        activity = a;
    }

    @Override
    public void inflate(Menu menu) {
        add(menu, R.string.file_overlay,()->FileAction.useAsOverlay(scontext.getContext(), file));

        inflateCopyTo(menu);

        add(menu, R.string.clipboard_copy,()->FileAction.copyToClipboard(scontext.getContext(), file));
        add(menu, R.string.file_send, ()->FileAction.sendTo(scontext.getContext(), file));

        inflateManipulate(menu);
    }

    protected void inflateCopyTo(Menu menu) {
        add(menu, R.string.file_copy, ()->FileAction.copyToDir(scontext.getContext(), file));
    }


    protected void inflateManipulate(Menu menu) {
        add(menu, R.string.file_view, ()-> FileAction.view(scontext.getContext(), file));
        add(menu, R.string.file_rename, ()->FileAction.rename(scontext, activity, file));
        add(menu, R.string.file_delete, ()->FileAction.delete(scontext, activity, file));
        add(menu, R.string.file_reload, ()->FileAction.reloadPreview(scontext, file));
        add(menu, R.string.file_mock, ()->FileAction.useForMockLocation(scontext.getContext(), file));
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
}
