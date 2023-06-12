package ch.bailu.aat.menus;


import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.aat_lib.util.fs.FileAction;
import ch.bailu.foc.Foc;

public final class ResultFileMenu extends FileMenu {

    private final String targetPrefix, targetExtendsion;

    private final ActivityContext context;

    private MenuItem saveCopy;

    public ResultFileMenu(ActivityContext context, Foc file, String prefix, String extension) {
        super(context, file);
        this.context = context;
        targetExtendsion = extension;
        targetPrefix = prefix;
    }


    @Override
    protected void inflateCopyTo(Menu menu) {
        saveCopy = menu.add(Res.str().edit_save_copy());
    }


    @Override
    protected void inflateManipulate(Menu menu) {
    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == saveCopy) {
            FileAction.copyToDir(context.getAppContext(), file,
                    AppDirectory.getDataDirectory(context.getAppContext().getDataDirectory(), AppDirectory.DIR_OVERLAY),
                    targetPrefix, targetExtendsion);
            return true;
        }

        return super.onItemClick(item);
    }
}
