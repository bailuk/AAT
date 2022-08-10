package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.dispatcher.CustomFileSource;
import ch.bailu.aat.menus.ResultFileMenu;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.OsmApiConfiguration;
import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.BusyViewControl;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.msg.ErrorMsgView;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.OsmApiEditorView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.InfoID;


public abstract class AbsOsmApiActivity extends ActivityContext implements OnClickListener {


    private ImageButtonViewGroup download;
    private BusyViewControl      downloadBusy;

    private View                 fileMenu;

    private NodeListView         list;
    private OsmApiConfiguration  configuration;

    protected OsmApiEditorView   editorView;

    private ErrorMsgView downloadError;


    protected final UiTheme theme = AppTheme.search;


    private final BroadcastReceiver onFileTaskChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDownloadStatus();
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configuration = createApiConfiguration(AppIntent.getBoundingBox(getIntent()));

        setContentView(createContentView());

        addSource(new CustomFileSource(getAppContext(), configuration.getResultFile().getPath()));
        addTarget(list, InfoID.FILEVIEW);

        OldAppBroadcaster.register(this, onFileTaskChanged,
                AppBroadcaster.FILE_BACKGROND_TASK_CHANGED);
    }


    private View createContentView()  {
        MainControlBar bar = createControlBar();

        ContentView contentView = new ContentView(this, theme);
        contentView.add(bar);

        contentView.add(downloadErrorView());
        contentView.add(fileErrorView());

        contentView.add(getErrorView());
        contentView.add(createMainContentView(contentView));

        addDownloadButton(bar);
        addCustomButtons(bar);
        addButtons(bar);

        return contentView;
    }

    private View downloadErrorView() {
        downloadError = new ErrorMsgView(this);

        return downloadError;
    }

    private View fileErrorView() {
        final ErrorMsgView fileError = new ErrorMsgView(this);

        addTarget((iid, info) -> fileError.displayError(getServiceContext(), info.getFile()),
                InfoID.FILEVIEW);
        return fileError;
    }


    private void addDownloadButton(MainControlBar bar) {
        download = bar.addImageButton(R.drawable.go_bottom_inverse);
        downloadBusy = new BusyViewControl(download);

        download.setOnClickListener(this);

        ToolTip.set(download, R.string.tt_nominatim_query);

        setDownloadStatus();
    }


    private void setDownloadStatus() {
        if (configuration.isTaskRunning(getServiceContext())) downloadBusy.startWaiting();
        else downloadBusy.stopWaiting();

        downloadError.displayError(configuration.getException());

    }

    private void addButtons(MainControlBar bar) {
        fileMenu = bar.addImageButton(R.drawable.edit_select_all_inverse);
    }


    protected View createMainContentView(ContentView contentView) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(createEditorView());
        layout.addView(createNodeListView(contentView));
        return layout;
    }

    protected View createNodeListView(ContentView contentView) {
        list = new NodeListView(this);
        return list;
    }

    private View createEditorView() {
        editorView = new OsmApiEditorView(this, configuration, theme);
        return editorView;
    }


    private MainControlBar createControlBar() {
        MainControlBar bar = new MainControlBar(this);
        bar.setOnClickListener1(this);


        return bar;
    }


    protected abstract OsmApiConfiguration createApiConfiguration(BoundingBoxE6 boundingBox);
    protected abstract void addCustomButtons(MainControlBar bar);

    protected OsmApiConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void onClick(View v) {
        if (v==download) {
            download();

        } else if (v == fileMenu) {
             showFileMenu(v);
        }


    }


    private void download() {
        if (configuration.isTaskRunning(getServiceContext())) {
            configuration.stopTask(getServiceContext());
        } else {
            configuration.startTask(getAppContext());
        }
    }


    private void showFileMenu(View parent) {

        final String targetPrefix = getTargetFilePrefix();
        final String targetExtension = configuration.getFileExtension();

        new ResultFileMenu(this, configuration.getResultFile(),
                targetPrefix, targetExtension).showAsPopup(this, parent);
    }

    private String getTargetFilePrefix() {
        try {
            final String query = TextBackup.read(configuration.getQueryFile());
            return OsmApiConfiguration.getFilePrefix(query);

        } catch (Exception e) {
            return OsmApiConfiguration.getFilePrefix("");
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
