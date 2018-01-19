package ch.bailu.aat.menus;


import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.util_java.foc.Foc;

public class ResultFileMenu extends FileMenu {

    private final String targetPrefix, targetExtendsion;


    private MenuItem saveCopy;

    public ResultFileMenu(AbsServiceLink a, Foc f, String prefix, String extension) {
        super(a, f);
        targetExtendsion = extension;
        targetPrefix = prefix;
    }


    protected void inflateCopyTo(Menu menu) {
        saveCopy = menu.add(ToDo.translate("Als overlay speichern"));
    }


    protected void inflateManipulate(Menu menu) {
    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == saveCopy) {
            FileAction.copyToDir(scontext.getContext(), file,
                    AppDirectory.getDataDirectory(scontext.getContext(), AppDirectory.DIR_OVERLAY),
                    targetPrefix, targetExtendsion);
            return true;
        }

        return super.onItemClick(item);
    }
}
