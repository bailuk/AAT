package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.view.View;

import org.mapsforge.core.model.LatLong;

import javax.annotation.Nonnull;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.EditorMenu;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.editor.EditorInterface;

public final class EditorBarLayer extends ControlBarLayer {

    private final View menu, add, remove, up, down,
            undo, redo;


    private final MapContext mcontext;
    private final ServicesInterface services;

    private final EditorNodeViewLayer selector;
    private final GpxDynLayer content;


    private final EditorSourceInterface edit;

    public EditorBarLayer(AppContext appContext, Context context, MapContext mc, DispatcherInterface d,
                          int iid, EditorSourceInterface e) {
        super(mc, new ControlBar(
                context,
                getOrientation(LEFT), AppTheme.bar), LEFT);

        edit = e;
        mcontext=mc;
        services = appContext.getServices();

        content = new GpxDynLayer(appContext.getStorage(), mc, appContext.getServices());
        selector = new EditorNodeViewLayer(appContext, context, mc, e);

        ControlBar bar = getBar();

        menu = bar.addImageButton(R.drawable.open_menu);

        add = bar.addImageButton(R.drawable.list_add);
        ToolTip.set(add, Res.str().tt_edit_add());

        remove = bar.addImageButton(R.drawable.list_remove);
        ToolTip.set(remove, Res.str().tt_edit_remove());

        up = bar.addImageButton(R.drawable.go_up);
        ToolTip.set(up, Res.str().tt_edit_up());

        down = bar.addImageButton(R.drawable.go_down);
        ToolTip.set(down, Res.str().tt_edit_down());

        redo = bar.addImageButton(R.drawable.edit_redo);
        ToolTip.set(redo, Res.str().tt_edit_redo());

        undo = bar.addImageButton(R.drawable.edit_undo);
        ToolTip.set(undo, Res.str().tt_edit_undo());

        d.addTarget(selector, iid);
        d.addTarget(content, iid);
    }


    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {
        super.onLayout(c, l, t, r, b);
        selector.onLayout(c, l, t, r,b);
    }


    @Override
    public void drawInside(MapContext p) {
        content.drawInside(p);
        if (isBarVisible()) {
            selector.drawInside(p);
        }
    }


    @Override
    public void drawForeground(MapContext p) {
        content.drawForeground(p);
        if (isBarVisible()) {
            selector.drawForeground(p);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        final EditorInterface editor = edit.getEditor();

        if (v==add)    {
            LatLong p = mcontext.getMapView().getMapViewPosition().getCenter();
            editor.add(new GpxPoint(p, services.getElevationService().getElevation(p.getLatitudeE6(), p.getLongitudeE6()), 0));
        }
        else if (v==remove) editor.remove();

        else if (v==up)      editor.up();
        else if (v==down)    editor.down();
        else if (v==undo)    editor.undo();
        else if (v==redo)    editor.redo();
        else if (v==menu)    new EditorMenu(v.getContext(), services, editor, edit.getFile())
                .showAsPopup(v.getContext(), v);
    }



    @Override
    public void onShowBar() {
        selector.showAtRight();
    }

    @Override
    public void onHideBar() {
        selector.hide();
    }


    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
        content.onPreferencesChanged(s,key);
        selector.onPreferencesChanged(s,key);
    }


    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
