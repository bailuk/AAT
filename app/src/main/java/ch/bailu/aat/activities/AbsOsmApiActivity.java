package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import org.mapsforge.map.rendertheme.renderinstruction.Line;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.dispatcher.CustomFileSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.menus.ResultFileMenu;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.BusyViewControl;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ErrorView;
import ch.bailu.aat.views.MyImageButton;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.OsmApiEditorView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.LabelTextView;
import ch.bailu.aat.views.preferences.TitleView;


public abstract class AbsOsmApiActivity extends ActivityContext implements OnClickListener {


    private MyImageButton   download;
    private BusyViewControl downloadBusy;

    private View               fileMenu;

    private NodeListView       list;
    protected OsmApiHelper       osmApi;

    protected OsmApiEditorView   editorView;


    private final BroadcastReceiver onFileTaskChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDownloadBusy();
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            osmApi = getApiHelper(AppIntent.getBoundingBox(getIntent()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        setContentView(createContentView());

        addSource(new CustomFileSource(getServiceContext(), osmApi.getResultFile().getPath()));
        addTarget(list, InfoID.FILEVIEW);

        AppBroadcaster.register(this, onFileTaskChanged,
                AppBroadcaster.FILE_BACKGROND_TASK_CHANGED);
    }


    private View createContentView()  {
        MainControlBar bar = createControlBar();

        ContentView contentView = new ContentView(this);
        contentView.add(bar);
        contentView.add(errorView());
        contentView.add(createMainContentView());

        addDownloadButton(bar);
        addCustomButtons(bar);
        addButtons(bar);

        return contentView;
    }

    private View errorView() {
        final ErrorView fileError = new ErrorView(this);

        addTarget((iid, info) -> {
            fileError.displayError(getServiceContext(), info.getFile());
        }, InfoID.FILEVIEW);
        return fileError;
    }


    private void addDownloadButton(MainControlBar bar) {
        download = bar.addImageButton(R.drawable.go_bottom_inverse);
        downloadBusy = new BusyViewControl(download);

        download.setOnClickListener(this);

        ToolTip.set(download, R.string.tt_nominatim_query);

        setDownloadBusy();
    }


    private void setDownloadBusy() {
        if (osmApi.isTaskRunning(getServiceContext())) downloadBusy.startWaiting();
        else downloadBusy.stopWaiting();

    }

    private void addButtons(MainControlBar bar) {
        fileMenu = bar.addImageButton(R.drawable.edit_select_all_inverse);
    }


    protected View createMainContentView() {
        PercentageLayout percentage = new PercentageLayout(this);
        percentage.add(createEditorView(), 20);
        percentage.add(createNodeListView(), 80);

        return percentage;
    }

    protected View createNodeListView() {
        list = new NodeListView(getServiceContext(), this);
        return list;
    }

    private View createEditorView() {
        editorView = new OsmApiEditorView(this, osmApi);
        return editorView;
    }


    private MainControlBar createControlBar() {
        MainControlBar bar = new MainControlBar(this);
        bar.setOnClickListener1(this);


        return bar;
    }


    protected abstract OsmApiHelper getApiHelper(BoundingBoxE6 boundingBox) throws SecurityException, IOException;
    protected abstract void addCustomButtons(MainControlBar bar);


    @Override
    public void onClick(View v) {
        if (v==download) {
            download();

        } else if (v == fileMenu) {
             showFileMenu(v);
        }


    }


    private void download() {
        if (osmApi.isTaskRunning(getServiceContext())) {
            osmApi.stopTask(getServiceContext());
        } else {
            osmApi.startTask(getServiceContext());
        }
    }


    private void showFileMenu(View parent) {

        final String targetPrefix = getTargetFilePrefix();
        final String targetExtension = osmApi.getFileExtension();

        new ResultFileMenu(this, osmApi.getResultFile(),
                targetPrefix, targetExtension).showAsPopup(this, parent);
    }

    private String getTargetFilePrefix() {
        try {
            final String query = TextBackup.read(osmApi.getQueryFile());
            return OsmApiHelper.getFilePrefix(query);

        } catch (Exception e) {
            return OsmApiHelper.getFilePrefix("");
        }
    }


    protected void insertLine(String s) {
        editorView.insertLine(s);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(onFileTaskChanged);
        super.onDestroy();
    }
}
