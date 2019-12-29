package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.dispatcher.CustomFileSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.menus.ResultFileMenu;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.background.DownloadTask;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.BusyViewControl;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.MyImageButton;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.OsmApiEditorView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.util_java.foc.Foc;


public abstract class AbsOsmApiActivity extends ActivityContext implements OnClickListener {


    private MyImageButton download;
    private BusyViewControl downloadBusy;

    private View               fileMenu;

    private NodeListView       list;
    private OsmApiHelper       osmApi;

    private OsmApiEditorView   editorView;


    private final BroadcastReceiver onDownloadsChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            new InsideContext(getServiceContext()) {
                @Override
                public void run() {
                    final DownloadTask task =
                            getServiceContext().getBackgroundService().findDownloadTask(osmApi.getResultFile());

                    if (task != null) {
                        downloadBusy.startWaiting();
                    } else {
                        downloadBusy.stopWaiting();
                    }

                }
            };
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            osmApi = getApiHelper(AppIntent.getBoundingBox(getIntent()));
        } catch (Exception e) {
            AppLog.e(this,e);
        }
        setContentView(createContentView());

        addSource(new CustomFileSource(getServiceContext(),osmApi.getResultFile().getPath()));
        addTarget(list, InfoID.FILEVIEW);

        setQueryTextFromIntent();

        AppBroadcaster.register(this, onDownloadsChanged, AppBroadcaster.ON_DOWNLOADS_CHANGED);
    }


    private void setQueryTextFromIntent() {
        String query = queryFromIntent(getIntent());
        if (query != null) {
            editorView.setText(query);
        }
    }


    public static String queryFromIntent(Intent intent) {
        Uri  uri = intent.getData();
        if (uri != null) return queryFromUri(uri);
        return null;
    }


    public static String queryFromUri(Uri uri) {

        String query = uri.getEncodedQuery();
        if (query != null) {
            Uri n = Uri.parse("http://localhost/query?" + uri.getEncodedQuery()); // we need a hierarchical url
            String query_parameter = n.getQueryParameter("q");
            if (query_parameter != null) {
                query_parameter = query_parameter.replace('\n', ',');
                return query_parameter;
            }
        }
        return null;
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

        new InsideContext(getServiceContext()) {
            @Override
            public void run() {
                if (getServiceContext().getBackgroundService().findDownloadTask(osmApi.getResultFile()) != null)
                    downloadBusy.startWaiting();
            }
        };

        ToolTip.set(download, R.string.tt_nominatim_query);
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
        new InsideContext(getServiceContext()) {
            @Override
            public void run() {
                try {
                    BackgroundService background = getServiceContext().getBackgroundService();

                    DownloadTask task = getServiceContext().getBackgroundService().findDownloadTask(osmApi.getResultFile());

                    if (task != null) {
                        task.stopProcessing();

                    } else {
                        String query = editorView.toString();

                        task = new ApiQueryTask(
                                getServiceContext().getContext(),
                                osmApi.getUrl(query),
                                osmApi.getResultFile(),
                                query,
                                osmApi.getQueryFile());

                        background.process(task);
                    }

                } catch (Exception e) {
                    AppLog.e(AbsOsmApiActivity.this, e);
                }
            }
        };
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
        unregisterReceiver(onDownloadsChanged);
        super.onDestroy();
    }




    private static class ApiQueryTask extends DownloadTask {
        private final String queryString;
        private final Foc queryFile;


        public ApiQueryTask(Context c, String source, Foc target, String qs, Foc qf) {
            super(c, source, target);
            queryString = qs;
            queryFile   = qf;
        }


        @Override
        public long bgOnProcess(ServiceContext sc) {
            try {
                long size = bgDownload();
                TextBackup.write(queryFile, queryString);

                AppBroadcaster.broadcast(sc.getContext(),
                        AppBroadcaster.FILE_CHANGED_ONDISK, getFile(), getSource());

                return size;
            } catch (Exception e) {
                logError(e);
                return 1;
            }
        }

        @Override
        protected void logError(Exception e) {
            AppLog.e(getContext(), e);
        }
    }
}
