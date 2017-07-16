package ch.bailu.aat.menus;


import android.app.Activity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tileremover.SelectedTileDirectoryInfo;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppLog;

public class RemoveTilesMenu extends AbsMenu {

    private MenuItem removeScanned, removeAll;

    private final ServiceContext scontext;
    private final Activity acontext;

    private final SelectedTileDirectoryInfo info;

    public RemoveTilesMenu(ServiceContext sc, Activity ac) {
        acontext = ac;
        scontext = sc;

        info = sc.getTileRemoverService().getInfo();


    }





    @Override
    public void inflate(Menu menu) {
        if (info.index == 0) {
            removeScanned = menu.add("Remove old tiles*");
            removeAll = menu.add("Clear entire cache*");
        } else {
            removeScanned = menu.add("Remove old tiles in *" + info.name);
            removeAll = menu.add("Clear cache in *" + info.name);

        }
    }


    @Override
    public void inflateWithHeader(ContextMenu menu) {
        menu.setHeaderTitle(info.name);
        menu.setHeaderIcon(R.drawable.user_trash_inverse);
        inflate(menu);
    }

    @Override
    public void prepare(Menu menu) {

    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == removeScanned) {
            scontext.lock();
            scontext.getTileRemoverService().getState().remove();
            scontext.free();

        } else if (item == removeAll) {
            new AppDialog() {
                @Override
                protected void onPositiveClick() {
                    scontext.lock();
                    scontext.getTileRemoverService().getState().removeAll();
                    scontext.free();
                    AppLog.i(scontext.getContext(), "Removed* " + info.directory);
                }
            }.displayYesNoDialog(
                    acontext,
                    "Empty cache*",
                    "Remove all files in* " + info.directory);
        }

        return false;
    }

}
