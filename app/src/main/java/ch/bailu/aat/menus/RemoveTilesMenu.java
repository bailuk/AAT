package ch.bailu.aat.menus;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tileremover.SelectedTileDirectoryInfo;
import ch.bailu.aat.util.Res;
import ch.bailu.aat.util.ui.AppDialog;

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
        Context c = scontext.getContext();

        if (info.index == 0) {
            removeScanned = menu.add(c.getString(R.string.p_remove_old));
            removeAll = menu.add(c.getString(R.string.p_remove_all));
        } else {
            removeScanned = menu.add(c.getString(R.string.p_remove_old_in) + info.name);
            removeAll = menu.add(c.getString(R.string.p_remove_all_in) + info.name);

        }
    }


    @Override
    public Drawable getIcon() {
        return Res.d(scontext.getContext(), R.drawable.user_trash_inverse);
    }

    @Override
    public String getTitle() {
        return info.name;
    }

    @Override
    public void prepare(Menu menu) {

    }

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == removeScanned) {
            new InsideContext(scontext) {
                @Override
                public void run() {
                    scontext.getTileRemoverService().getState().remove();
                }
            };

        } else if (item == removeAll) {
            new AppDialog() {
                @Override
                protected void onPositiveClick() {
                    new InsideContext(scontext) {
                        @Override
                        public void run() {
                            scontext.getTileRemoverService().getState().removeAll();
                        }
                    };
                }
            }.displayYesNoDialog(
                    acontext,
                    scontext.getContext().getString(R.string.p_remove_all),
                    scontext.getContext().getString(R.string.p_remove_all_in) + " " + info.directory);
        }

        return false;
    }
}
