package ch.bailu.aat.views.map.overlay.control;

import android.view.View;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.file.FileAction;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.PreviewView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.gpx.InfoViewNodeSelectorOverlay;

public class FileControlBar extends ControlBarOverlay{


    @Override
    public void onShowBar() {
        selector.showAtRight();
    }


    private final PreviewView        preview;
    private final AbsGpxListActivity activity;
    private final Selector           selector;

    private final View           action, overlay, reloadPreview, delete;

    public FileControlBar(OsmInteractiveView osm, AbsGpxListActivity a) {
        super(osm, new ControlBar(
                osm.getContext(),
                getOrientation(LEFT)), LEFT);

        final ControlBar bar = getBar();

        activity = a;

        selector = new Selector(osm, InfoID.LIST_SUMMARY);
        preview = new PreviewView(a.getServiceContext());

        bar.addView(preview);
        action = bar.addImageButton(R.drawable.edit_select_all);
        overlay = bar.addImageButton(R.drawable.view_paged);
        reloadPreview = bar.addImageButton(R.drawable.view_refresh);
        delete = bar.addImageButton(R.drawable.user_trash);
        
        preview.setOnClickListener(this);
        preview.setOnLongClickListener(selector);


        ToolTip.set(action, R.string.tt_menu_file);
        ToolTip.set(overlay, R.string.file_overlay);
        ToolTip.set(reloadPreview, R.string.file_reload);
        ToolTip.set(delete, R.string.file_delete);

    }


    
    private class Selector extends InfoViewNodeSelectorOverlay {
        public Selector(OsmInteractiveView v, int id) {
            super(v, id);
        }

        @Override
        public void setSelectedNode(GpxInformation info, GpxPointNode node, int i) {
            super.setSelectedNode(info, node, i);
            preview.setFilePath(getPathFromNode(node));
            new SolidDirectoryQuery(getContext()).getPosition().setValue(i);
        }
        
        @Override
        public boolean onLongClick(View v) {
            activity.displayFile();
            return true;
        }
    }
    

    
    public String getPathFromNode(GpxPointNode node) {
        if (node != null) {
            return node.getAttributes().get("path");
        } else {
            return "";
        }
    }
    
    
    @Override
    public void draw(MapPainter p) {
        if (isVisible()) {
            selector.draw(p);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        GpxPointNode node =  selector.getSelectedNode();
        if (node != null) {
            File file = new File(node.getValue("path"));

            if (file.exists()) {
                if        (v == action) {
                    new FileMenu(activity, file).showAsPopup(activity, v);
                } else if (v == overlay) {
                    FileAction.useAsOverlay(activity, file);
                } else if (v == reloadPreview) {
                    FileAction.reloadPreview(activity.getServiceContext(), file);
                } else if (v == delete) {
                    FileAction.delete(activity.getServiceContext(), activity, file);
                } else if (v == preview) {
                    activity.displayFile();
                }
            }
        }


    }



    @Override
    public void onHideBar() {
        selector.hide();
    }


    @Override
    public void onContentUpdated(GpxInformation info) {
        selector.onContentUpdated(info);
    }


    @Override
    public void run() {}
}

