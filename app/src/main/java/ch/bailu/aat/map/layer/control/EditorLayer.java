package ch.bailu.aat.map.layer.control;

import android.content.SharedPreferences;
import android.view.View;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.ControlBar;

public class EditorLayer extends ControlBarLayer {

    private final View add, remove, up, down,
            save, saveAs, toggle, clear,
            undo, redo;


    private final ServiceContext scontext;
    private final MapContext mcontext;

    private final EditorNodeSelectorLayer selector;
    private final GpxDynLayer content;


    private final EditorHelper edit;

    public EditorLayer(MapContext mc, DispatcherInterface d,
                       int iid, EditorHelper e) {
        super(mc, new ControlBar(
                mc.getContext(),
                getOrientation(LEFT)), LEFT);

        edit = e;
        scontext=mc.getSContext();
        mcontext=mc;


        content = new GpxDynLayer(mc, -1);
        selector = new EditorNodeSelectorLayer(mc, e);


        ControlBar bar = getBar();

        add = bar.addImageButton(R.drawable.list_add);
        ToolTip.set(add, R.string.tt_edit_add);

        remove = bar.addImageButton(R.drawable.list_remove);
        ToolTip.set(remove, R.string.tt_edit_remove);

        up = bar.addImageButton(R.drawable.go_up);
        ToolTip.set(up, R.string.tt_edit_up);

        down = bar.addImageButton(R.drawable.go_down);
        ToolTip.set(down, R.string.tt_edit_down);

        toggle = bar.addImageButton(R.drawable.gtk_convert);
        ToolTip.set(toggle, R.string.tt_edit_convert);

        clear = bar.addImageButton(R.drawable.edit_clear_all);
        ToolTip.set(clear, R.string.tt_edit_clear);

        redo = bar.addImageButton(R.drawable.edit_redo);
        ToolTip.set(redo, R.string.tt_edit_redo);

        undo = bar.addImageButton(R.drawable.edit_undo);
        ToolTip.set(undo, R.string.tt_edit_undo);

        save = bar.addImageButton(R.drawable.document_save);
        ToolTip.set(save, R.string.tt_edit_save);

        saveAs = bar.addImageButton(R.drawable.document_save_as);
        ToolTip.set(saveAs, R.string.tt_edit_save_as);

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
    public void drawOnTop(MapContext p) {
        content.drawOnTop(p);
        if (isBarVisible()) {
            selector.drawOnTop(p);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        final EditorInterface editor = edit.getEditor();

        if (v==save)    editor.save();
        else if (v==saveAs)  editor.saveAs();
        else if (v==add)    {
            LatLong p = mcontext.getMetrics().getBoundingBox().getCenterPoint();
            editor.add(new GpxPoint(p, scontext.getElevationService().getElevation(p.getLatitudeE6(), p.getLongitudeE6()), 0));
        }
        else if (v==remove) editor.remove();

        else if (v==up)      editor.up();
        else if (v==down)    editor.down();
        else if (v==toggle)  editor.toggle();
        else if (v==clear)   editor.clear();
        else if (v==undo)    editor.undo();
        else if (v==redo)    editor.redo();
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
