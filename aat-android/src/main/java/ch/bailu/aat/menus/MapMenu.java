package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.PreferencesActivity;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.AndroidMapDirectories;
import ch.bailu.aat.preferences.map.SolidMapTileStack;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.preferences.SolidCheckListDialog;
import ch.bailu.aat.views.preferences.SolidStringDialog;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList;
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme;
import ch.bailu.foc.FocFactory;
import ch.bailu.foc_android.FocAndroidFactory;

public final class MapMenu extends AbsMenu {
    private MenuItem stack, overlays, reload, theme, preferences, map;

    private final MapContext mcontext;
    private final Context context;


    public MapMenu(Context c, MapContext mc) {
        context = c;
        mcontext = mc;
    }

    @Override
    public void inflate(Menu menu) {
        stack = menu.add(R.string.p_map);

        overlays = menu.add(R.string.p_overlay);
        overlays.setIcon(R.drawable.view_paged_inverse);

        SolidMapsForgeMapFile smapFile = new AndroidMapDirectories(context).createSolidFile();
        map = menu.add(smapFile.getLabel());
        theme = menu.add(new SolidRenderTheme(smapFile, new FocAndroidFactory(context)).getLabel());

        preferences = menu.add(R.string.intro_settings);

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
    public void prepare(Menu menu) {}

    @Override
    public boolean onItemClick(MenuItem item) {
        final Context c = context;
        final FocFactory foc = new FocAndroidFactory(c);
        final StorageInterface storage = new Storage(c);

        final SolidMapsForgeDirectory sdir = new SolidMapsForgeDirectory(storage, foc, new AndroidMapDirectories(c));
        final SolidRenderTheme stheme = new SolidRenderTheme(sdir, foc);
        if (item == stack) {
            new SolidCheckListDialog(c,
                    new SolidMapTileStack(stheme));
        } else if (item ==reload) {
                mcontext.getMapView().reDownloadTiles();

            } else if (item == overlays) {
            new SolidCheckListDialog(c, new SolidOverlayFileList(storage, foc));
        } else if (item == theme) {
            new SolidStringDialog(c, stheme);
        } else if (item == map) {
            new SolidStringDialog(c,new AndroidMapDirectories(c).createSolidFile());
        } else if (item == preferences) {
            MultiView.storeActive(c, PreferencesActivity.SOLID_KEY, 1);
            ActivitySwitcher.start(context, PreferencesActivity.class);
        } else {
            return false;
        }
        return true;
    }
}
