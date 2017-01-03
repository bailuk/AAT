package ch.bailu.aat.map.osm.overlay.control;

import android.view.View;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.PreviewView;
import ch.bailu.aat.map.osm.OsmInteractiveView;
import ch.bailu.aat.map.osm.overlay.MapPainter;
import ch.bailu.aat.map.osm.overlay.gpx.NodeViewOverlay;

public class FileControlBar extends ControlBarOverlay{


    @Override
    public void onShowBar() {
        selector.showAtRight();
    }



    private final PreviewView        preview;
    private final AbsGpxListActivity acontext;
    private final Selector           selector;

    private final View           action, overlay, reloadPreview, delete;

    private Iterator iterator = Iterator.NULL;
    private String selectedFile = null;


    public FileControlBar(OsmInteractiveView osm, AbsGpxListActivity a) {
        super(osm, new ControlBar(
                osm.getContext(),
                getOrientation(LEFT)), LEFT);

        final ControlBar bar = getBar();

        acontext = a;



        selector = new Selector(osm);
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

        acontext.addTarget(selector, InfoID.LIST_SUMMARY);
    }

    public void setIterator(Iterator i) {
        iterator = i;
    }


    private class Selector extends NodeViewOverlay {
        public Selector(OsmInteractiveView v) {
            super(v);
        }

        final HtmlBuilderGpx builder = new HtmlBuilderGpx(getContext());


        final ContentDescription summaryData[] = {

                new DateDescription(getContext()),
                new TimeDescription(getContext()),

                new DistanceDescription(getContext()),
                new AverageSpeedDescription(getContext()),
                new MaximumSpeedDescription(getContext()),
                new CaloriesDescription(getContext()),
        };

        @Override
        public void setSelectedNode(GpxInformation info, GpxPointNode node, int i) {
            new SolidDirectoryQuery(getContext()).getPosition().setValue(i);

            iterator.moveToPosition(i);

            selectedFile = iterator.getInfo().getPath();

            preview.setFilePath(selectedFile);

            builder.clear();
            builder.appendHeader(iterator.getInfo().getName());
            for (ContentDescription d: summaryData) {
                d.onContentUpdated(iterator.getInfoID(), iterator.getInfo());
                builder.append(d);
                builder.append("<br>");
            }

            setHtmlText(builder.toString());

        }

        @Override
        public boolean onLongClick(View v) {
            acontext.displayFile();
            return true;
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
        if (node != null && selectedFile != null) {
            File file = new File(selectedFile);

            if (file.exists()) {
                if        (v == action) {
                    new FileMenu(acontext, file).showAsPopup(acontext, v);
                } else if (v == overlay) {
                    FileAction.useAsOverlay(acontext, file);
                } else if (v == reloadPreview) {
                    FileAction.reloadPreview(acontext.getServiceContext(), file);
                } else if (v == delete) {
                    FileAction.delete(acontext.getServiceContext(), acontext, file);
                } else if (v == preview) {
                    acontext.displayFile();
                }
            }
        }
    }



    @Override
    public void onHideBar() {
        selector.hide();
    }


    @Override
    public void run() {}
}

