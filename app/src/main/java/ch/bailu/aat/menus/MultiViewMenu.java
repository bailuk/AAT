package ch.bailu.aat.menus;


import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.views.description.MultiView;

public class MultiViewMenu extends AbsMenu {

    private final MultiView mview;

    public MultiViewMenu(MultiView mv) {
        mview = mv;
    }

    @Override
    public void inflate(Menu menu) {
        mview.inflateMenu(menu);
    }

    @Override
    public void inflateWithHeader(ContextMenu menu) {
        inflate(menu);
    }

    @Override
    public void prepare(Menu menu) {}

    @Override
    public boolean onItemClick(MenuItem item) {
        mview.setActive(item.getItemId());
        return true;
    }

}
