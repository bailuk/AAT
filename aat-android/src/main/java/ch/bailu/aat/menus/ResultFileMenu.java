package ch.bailu.aat.menus;


import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public final class ResultFileMenu extends FileMenu {

    private final String targetPrefix, targetExtendsion;


    private MenuItem saveCopy;

    public ResultFileMenu(AbsServiceLink a, Foc f, String prefix, String extension) {
        super(a, f);
        targetExtendsion = extension;
        targetPrefix = prefix;
    }


    @Override
    protected void inflateCopyTo(Menu menu) {
        saveCopy = menu.add(
                scontext.getContext().getString(R.string.query_save_copy));
    }


    @Override
    protected void inflateManipulate(Menu menu) {
    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == saveCopy) {
            FileAction.copyToDir(scontext.getContext(), file,
                    AppDirectory.getDataDirectory(new AndroidSolidDataDirectory(scontext.getContext()), AppDirectory.DIR_OVERLAY),
                    targetPrefix, targetExtendsion);
            return true;
        }

        return super.onItemClick(item);
    }
}
