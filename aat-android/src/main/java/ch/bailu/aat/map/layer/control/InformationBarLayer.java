package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.view.View;

import javax.annotation.Nonnull;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.LocationMenu;
import ch.bailu.aat.menus.MapMenu;
import ch.bailu.aat.menus.MapQueryMenu;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidLegend;
import ch.bailu.aat_lib.preferences.map.SolidMapGrid;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class InformationBarLayer extends ControlBarLayer {

    private final ImageButtonViewGroup map, search, location;

    private final AbsNodeViewLayer selector;
    private final MapContext mcontext;
    private final Context context;


    public InformationBarLayer(Context context, ServicesInterface services, MapContext mc, DispatcherInterface d) {
        super(mc,new ControlBar(context, getOrientation(RIGHT), AppTheme.bar), RIGHT);

        this.context = context;
        StorageInterface storage = new Storage(context);
        mcontext = mc;

        final SolidIndexList sgrid, slegend;


        sgrid = new SolidMapGrid(storage, mc.getSolidKey());
        slegend = new SolidLegend(storage, mc.getSolidKey());

        ControlBar bar = getBar();

        map = bar.addImageButton(R.drawable.open_menu);

        View grid=bar.addSolidIndexButton(sgrid);
        View legend=bar.addSolidIndexButton(slegend);

        search = bar.addImageButton(R.drawable.edit_find);
        location = bar.addImageButton(R.drawable.find_location);

        selector = new NodeViewLayer(context, services, storage, mc);


        ToolTip.set(grid, Res.str().tt_info_grid());
        ToolTip.set(legend,Res.str().tt_info_legend());
        ToolTip.set(location, Res.str().tt_info_location());

        d.addTarget(selector, InfoID.ALL);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == map) {
            new MapMenu(context, mcontext).showAsPopup(v.getContext(),v);

        } else if (v==search) {
            new MapQueryMenu(context, mcontext).showAsPopup(v.getContext(), v);

        } else if (v==location) {
            new LocationMenu(context, mcontext.getMapView()).showAsPopup(v.getContext(), location);
        }
    }

    @Override
    public void onShowBar() {
        selector.showAtLeft();
    }


    @Override
    public void onHideBar() {
        selector.hide();
    }


    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {
        super.onLayout(c, l, t, r, b);
        selector.onLayout(c, l, t, r,b);
    }

    @Override
    public void drawForeground(MapContext mcontext) {
        if (isBarVisible()) selector.drawForeground(mcontext);
    }

    @Override
    public void drawInside(MapContext mcontext) {
        if (isBarVisible()) selector.drawInside(mcontext);
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
        selector.onPreferencesChanged(s, key);
    }
}
