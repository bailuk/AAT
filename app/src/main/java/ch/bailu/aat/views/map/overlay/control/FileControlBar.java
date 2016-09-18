package ch.bailu.aat.views.map.overlay.control;

import java.io.File;

import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.FileAction;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.PreviewView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.gpx.InfoViewNodeSelectorOverlay;

public class FileControlBar extends ControlBarOverlay{


    @Override
    public void showBar() {
        showBarAtLeft();
        selector.showAtRight();
    }


    private final PreviewView        preview;
    private final AbsGpxListActivity activity;
    private final Selector           selector;

    private final View           action, overlay, reloadPreview, delete;

    public FileControlBar(OsmInteractiveView osm, AbsGpxListActivity a) {
        super(osm, new ControlBar(
                osm.getContext(),
                LinearLayout.VERTICAL));

        final ControlBar bar = getBar();

        activity = a;

        selector = new Selector(osm, GpxInformation.ID.INFO_ID_LIST_SUMMARY);
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
            new SolidDirectory(getContext()).getPosition().setValue(i);
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
                    new FileAction(activity, file).showPopupMenu(v);
                } else if (v == overlay) {
                    new FileAction(activity, file).useAsOverlay();
                } else if (v == reloadPreview) {
                    new FileAction(activity, file).reloadPreview();
                } else if (v == delete) {
                    new FileAction(activity, file).delete();
                } else if (v == preview) {
                    activity.displayFile();
                }
            }
        }


    }


    @Override
    public void leftTab() {
        showBar();
    }


    @Override
    public void hideBar() {
        super.hideBar();
        selector.hide();
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        selector.updateGpxContent(info);
    }


    @Override
    public void run() {}
}

