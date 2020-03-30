package ch.bailu.aat.map.layer.control;

import android.content.SharedPreferences;
import android.view.View;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.EditorSourceInterface;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat.menus.EditorMenu;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.bar.ControlBar;

public final class EditorLayer extends ControlBarLayer {

    private final View menu, add, remove, up, down,
            undo, redo;


    private final ServiceContext scontext;
    private final MapContext mcontext;

    private final EditorNodeViewLayer selector;
    private final GpxDynLayer content;


    private final EditorSourceInterface edit;

    public EditorLayer(MapContext mc, DispatcherInterface d,
                       int iid, EditorSourceInterface e) {
        super(mc, new ControlBar(
                mc.getContext(),
                getOrientation(LEFT), AppTheme.bar), LEFT);

        edit = e;
        scontext=mc.getSContext();
        mcontext=mc;


        content = new GpxDynLayer(mc);
        selector = new EditorNodeViewLayer(mc, e);


        ControlBar bar = getBar();

        menu = bar.addImageButton(R.drawable.open_menu);

        add = bar.addImageButton(R.drawable.list_add);
        ToolTip.set(add, R.string.tt_edit_add);

        remove = bar.addImageButton(R.drawable.list_remove);
        ToolTip.set(remove, R.string.tt_edit_remove);

        up = bar.addImageButton(R.drawable.go_up);
        ToolTip.set(up, R.string.tt_edit_up);

        down = bar.addImageButton(R.drawable.go_down);
        ToolTip.set(down, R.string.tt_edit_down);

        redo = bar.addImageButton(R.drawable.edit_redo);
        ToolTip.set(redo, R.string.tt_edit_redo);

        undo = bar.addImageButton(R.drawable.edit_undo);
        ToolTip.set(undo, R.string.tt_edit_undo);

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
            editor.add(new GpxPoint(p, scontext.getElevationService().getElevation(p.getLatitudeE6(), p.getLongitudeE6()), 0));
        }
        else if (v==remove) editor.remove();

        else if (v==up)      editor.up();
        else if (v==down)    editor.down();
        else if (v==undo)    editor.undo();
        else if (v==redo)    editor.redo();
        else if (v==menu)    new EditorMenu(scontext, editor, edit.getFile())
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
    public void onSharedPreferenceChanged(SharedPreferences p, String key) {
        content.onSharedPreferenceChanged(p, key);
        selector.onSharedPreferenceChanged(p, key);
    }


    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
