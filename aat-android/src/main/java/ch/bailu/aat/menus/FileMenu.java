package ch.bailu.aat.menus;

import android.graphics.drawable.Drawable;
import android.view.Menu;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.util.fs.AndroidFileAction;
import ch.bailu.aat_lib.util.fs.FileAction;
import ch.bailu.foc.Foc;

public class FileMenu extends AbsMenu {
    protected final Foc file;

    private final ActivityContext activity;
    //protected final ServiceContext scontext;

    public FileMenu(ActivityContext a, Foc f) {
        file = f;
        //scontext = a.getServiceContext();
        activity = a;
    }

    @Override
    public void inflate(Menu menu) {
        add(menu, R.string.file_overlay,()-> AndroidFileAction.useAsOverlay(activity, file));

        inflateCopyTo(menu);

        add(menu, R.string.clipboard_copy,()-> AndroidFileAction.copyToClipboard(activity, file));
        add(menu, R.string.file_send, ()-> AndroidFileAction.sendTo(activity, file));

        inflateManipulate(menu);
    }

    protected void inflateCopyTo(Menu menu) {
        add(menu, R.string.file_copy, ()-> AndroidFileAction.copyToDir(activity, activity.getAppContext(), file));
    }


    protected void inflateManipulate(Menu menu) {
        add(menu, R.string.file_view, ()-> AndroidFileAction.view(activity, file));
        add(menu, R.string.file_rename, ()-> AndroidFileAction.rename(activity.getAppContext(), activity, file));
        add(menu, R.string.file_delete, ()-> AndroidFileAction.delete(activity.getAppContext(), activity, file));
        add(menu, R.string.file_reload, ()-> FileAction.reloadPreview(activity.getAppContext(), file));
        add(menu, R.string.file_mock, ()-> FileAction.useForMockLocation(activity.getAppContext(), file));
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
