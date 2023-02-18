package ch.bailu.aat.map.layer.control;

import android.content.Context;
import android.view.View;

import javax.annotation.Nonnull;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.FileAction;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.PreviewView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.description.AverageSpeedDescription;
import ch.bailu.aat_lib.description.CaloriesDescription;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.DateDescription;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.description.MaximumSpeedDescription;
import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.edge.Position;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFile;
import ch.bailu.aat_lib.service.directory.Iterator;
import ch.bailu.aat_lib.service.directory.SummaryConfig;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroidFactory;

public final class FileControlBarLayer extends ControlBarLayer {

    private final PreviewView preview;
    private final AbsGpxListActivity acontext;
    private final FileViewLayer selector;

    private final View           overlay, reloadPreview, delete;

    private Iterator iterator = Iterator.NULL;
    private Foc selectedFile = null;


    private final Storage storage;

    public FileControlBarLayer(AppContext appContext, MapContext mc, AbsGpxListActivity absGpxListActivity, SummaryConfig config) {
        super(mc, new ControlBar(
                absGpxListActivity,
                getOrientation(Position.LEFT), AppTheme.bar), Position.LEFT);

        final ControlBar bar = getBar();

        storage = new Storage(absGpxListActivity);
        acontext = absGpxListActivity;

        selector = new FileViewLayer(appContext, absGpxListActivity, mc);
        preview = new PreviewView(absGpxListActivity.getServiceContext(), config);

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
    public void onAttached() {}

    @Override
    public void onDetached() {}

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
        public FileViewLayer(AppContext appContext, Context context, MapContext mc) {
            super(appContext, context, mc);
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
        public void setSelectedNode(int IID, @Nonnull GpxInformation info, @Nonnull GpxPointNode node, int index) {
            super.setSelectedNode(IID, info, node, index);

            new SolidDirectoryQuery(new Storage(acontext), new FocAndroidFactory(acontext)).getPosition().setValue(index);

            iterator.moveToPosition(index);
            selectedFile = iterator.getInfo().getFile();
            preview.setFilePath(selectedFile);

            markupBuilder.appendHeader(iterator.getInfo().getFile().getName());
            for (ContentDescription d: summaryData) {
                d.onContentUpdated(iterator.getInfoID(), iterator.getInfo());
                markupBuilder.appendNl(d);
            }

            setHtmlText(markupBuilder);
        }

        @Override
        public void onClick(View v) {
            acontext.displayFile();
        }

        @Override
        public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
            selector.onPreferencesChanged(s, key);
        }

        @Override
        public void onAttached() {}

        @Override
        public void onDetached() {}

        @Override
        public boolean onLongClick(View view) {
            if (selectedFile != null) {
                new SolidOverlayFile(new Storage(acontext), new FocAndroidFactory(acontext), 0).setValueFromFile(selectedFile);
                return true;
            }
            return false;
        }

        @Override
        public boolean onTap(Point tapPos) {
            return false;
        }
    }
}
