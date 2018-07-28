package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.preferences.SolidRenderTheme;
import ch.bailu.aat.views.preferences.SolidCheckListDialog;
import ch.bailu.aat.views.preferences.SolidStringDialog;

public class MapMenu extends AbsMenu {
    private MenuItem map, overlays, reload, theme;

    private final MapContext mcontext;


    public MapMenu(MapContext mc) {
        mcontext = mc;
    }

    @Override
    public void inflate(Menu menu) {
        map = menu.add(R.string.p_map);

        overlays = menu.add(R.string.p_overlay);
        overlays.setIcon(R.drawable.view_paged_inverse);


        theme = menu.add(R.string.p_mapsforge_theme);

        reload = menu.add(R.string.tt_info_reload);
        reload.setIcon(R.drawable.view_refresh);



    }

    @Override
    public String getTitle() {
        return null;
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
        final Context c = mcontext.getContext();

        if (item == map) {
            new SolidCheckListDialog(new SolidMapTileStack(c));
        } else if (item ==reload) {
                mcontext.getMapView().reDownloadTiles();

            } else if (item == overlays) {
            new SolidCheckListDialog(new SolidOverlayFileList(c));
        } else if (item == theme){
            new SolidStringDialog(new SolidRenderTheme(c));

        } else {
            return false;
        }
        return true;
    }
}
