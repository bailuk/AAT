package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
import ch.bailu.aat.views.MyImageButton;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.OsmApiEditorView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;


public abstract class AbsOsmApiActivity extends ActivityContext implements OnClickListener {


    private MyImageButton   download;
    private BusyViewControl downloadBusy;

    private View               fileMenu;

    private NodeListView       list;
    private OsmApiHelper       osmApi;

    private OsmApiEditorView   editorView;


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
        contentView.add(createMainContentView());

        addDownloadButton(bar);
        addCustomButtons(bar);
        addButtons(bar);

        return contentView;
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
        editorView = new OsmApiEditorView(this, osmApi);
        list = new NodeListView(getServiceContext(), this);

        PercentageLayout percentage = new PercentageLayout(this);
        percentage.add(editorView, 30);
        percentage.add(list, 70);

        return percentage;
    }





    private MainControlBar createControlBar() {
        MainControlBar bar = new MainControlBar(this);
        bar.setOnClickListener1(this);


        return bar;
    }


    public abstract OsmApiHelper getApiHelper(BoundingBoxE6 boundingBox) throws SecurityException, IOException;
    public abstract void addCustomButtons(MainControlBar bar);


    @Override
    public void onClick(View v) {
        if (v==download) {
            download();

        } else if (v == fileMenu) {
            try {
                showFileMenu(v);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void download() {
        if (osmApi.isTaskRunning(getServiceContext())) {
            osmApi.stopTask(getServiceContext());
        } else {
            osmApi.startTask(getServiceContext(), editorView.toString());
        }
    }


    private void showFileMenu(View parent) throws IOException {
        final String query = TextBackup.read(osmApi.getQueryFile());
        final String prefix = OsmApiHelper.getFilePrefix(query);
        final String extension = osmApi.getFileExtension();

        new ResultFileMenu(this, osmApi.getResultFile(),
                prefix, extension).showAsPopup(this, parent);
    }


    public void insertLine(String s) {
        editorView.insertLine(s);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(onFileTaskChanged);
        super.onDestroy();
    }
}
