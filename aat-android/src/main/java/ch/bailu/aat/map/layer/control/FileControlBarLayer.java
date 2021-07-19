package ch.bailu.aat.map.layer.control;

import android.view.View;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat_lib.description.AverageSpeedDescription;
import ch.bailu.aat_lib.description.CaloriesDescription;
import ch.bailu.aat_lib.description.DateDescription;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.description.MaximumSpeedDescription;
import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.SolidOverlayFile;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.PreviewView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.foc.Foc;

public final class FileControlBarLayer extends ControlBarLayer {



    private final PreviewView preview;
    private final AbsGpxListActivity acontext;
    private final FileViewLayer selector;

    private final View           overlay, reloadPreview, delete;

    private Iterator iterator = Iterator.NULL;
    private Foc selectedFile = null;


    private final Storage storage;

    public FileControlBarLayer(MapContext mc, AbsGpxListActivity a) {
        super(mc, new ControlBar(
                mc.getContext(),
                getOrientation(LEFT), AppTheme.bar), LEFT);

        final ControlBar bar = getBar();

        storage = new Storage(mc.getContext());
        acontext = a;

        selector = new FileViewLayer(mc);
        preview = new PreviewView(a.getServiceContext());

        bar.add(preview);
        overlay = bar.addImageButton(R.drawable.view_paged);
        reloadPreview = bar.addImageButton(R.drawable.view_refresh);
        delete = bar.addImageButton(R.drawable.user_trash);

        preview.setOnClickListener(this);

        ToolTip.set(preview, R.string.tt_menu_file);
        ToolTip.set(overlay, R.string.file_overlay);
        ToolTip.set(reloadPreview, R.string.file_reload);
        ToolTip.set(delete, R.string.file_delete);

        acontext.addTarget(selector, InfoID.LIST_SUMMARY);
    }


    public void setIterator(Iterator i) {
        iterator = i;
    }


    @Override
    public void onShowBar() {
        selector.showAtRight();
    }


    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


    @Override
    public void drawForeground(MapContext mc) {
        if (isBarVisible()) {
            selector.drawForeground(mc);
        }
    }


    @Override
    public void drawInside(MapContext mc) {
        if (isBarVisible()) {
            selector.drawInside(mc);
        }
    }


    @Override
    public void onLayout(boolean c, int l, int t, int r, int b) {
        super.onLayout(c, l, t, r, b);
        selector.onLayout(c, l, t, r,b);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        GpxPointNode node =  selector.getSelectedNode();
        if (node != null && selectedFile != null) {
            Foc file = selectedFile;

            if (file.exists()) {
                if        (v == preview) {
                    new FileMenu(acontext, file).showAsPopup(acontext, v);
                } else if (v == overlay) {
                    FileAction.useAsOverlay(acontext, file);
                } else if (v == reloadPreview) {
                    FileAction.reloadPreview(acontext.getServiceContext(), file);
                } else if (v == delete) {
                    FileAction.delete(acontext.getServiceContext(), acontext, file);
                }
            }
        }
    }



    @Override
    public void onHideBar() {
        selector.hide();
    }


    private class FileViewLayer extends AbsNodeViewLayer {
        public FileViewLayer(MapContext mc) {
            super(mc);
        }


        final ContentDescription[] summaryData = {

                new DateDescription(),
                new TimeDescription(),

                new DistanceDescription(storage),
                new AverageSpeedDescription(storage),
                new MaximumSpeedDescription(storage),
                new CaloriesDescription(storage),
        };

        @Override
        public void setSelectedNode(int IID, GpxInformation info, GpxPointNode node, int i) {
            super.setSelectedNode(IID, info, node, i);

            new SolidDirectoryQuery(new Storage(acontext), new AndroidFocFactory(acontext)).getPosition().setValue(i);

            iterator.moveToPosition(i);

            selectedFile = iterator.getInfo().getFile();

            preview.setFilePath(selectedFile);

            html.appendHeader(iterator.getInfo().getFile().getName());
            for (ContentDescription d: summaryData) {
                d.onContentUpdated(iterator.getInfoID(), iterator.getInfo());
                html.append(d);
                html.append("<br>");
            }

            setHtmlText(html);

        }

        @Override
        public void onClick(View v) {
            acontext.displayFile();
        }

        @Override
        public boolean onTap(Point tapXY) {
            return false;
        }

        @Override
        public void onPreferencesChanged(StorageInterface s, String key) {
            selector.onPreferencesChanged(s, key);
        }

        @Override
        public void onAttached() {

        }

        @Override
        public void onDetached() {

        }

        @Override
        public boolean onLongClick(View view) {
            if (selectedFile != null) {
                new SolidOverlayFile(acontext,0).setValueFromFile(selectedFile);
                return true;
            }
            return false;
        }
    }


}

