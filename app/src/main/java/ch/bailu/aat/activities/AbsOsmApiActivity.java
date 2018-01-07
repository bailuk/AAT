package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.dispatcher.CustomFileSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.background.DownloadTask;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.EditTextTool;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.TagEditor;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.util_java.foc.Foc;


public abstract class AbsOsmApiActivity extends AbsDispatcher implements OnClickListener {

    private TagEditor          tagEditor;
    private BusyButton         download;
    private View               fileMenu;

    private NodeListView       list;

    private OsmApiHelper       osmApi;
    private BackgroundTask     request = BackgroundTask.NULL;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            osmApi = createUrlGenerator(AppIntent.getBoundingBox(getIntent()));
            AppBroadcaster.register(this, onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);
        } catch (Exception e) {
            AppLog.e(this,e);
        }
        setContentView(createContentView());

        addSource(new CustomFileSource(getServiceContext(),osmApi.getResultFile().getPath()));
        addTarget(list, InfoID.FILEVIEW);

        setQueryTextFromIntent();
    }


    private void setQueryTextFromIntent() {
        String query = queryFromIntent(getIntent());
        if (query != null) {
            tagEditor.setText(query);
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
            Uri n = Uri.parse("http://bailu.ch/query?"+uri.getEncodedQuery()); // we need a hierarchical url
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
        contentView.addView(bar);
        contentView.addView(createMainContentView(bar));
        return contentView;
    }


    protected View createMainContentView(MainControlBar bar) {
        View input = createTagEditor();
        list = new NodeListView(getServiceContext(), this);

        PercentageLayout percentage = new PercentageLayout(this);
        percentage.add(input, 30);
        percentage.add(list, 70);

        return percentage;
    }


    private View createTagEditor() {
        LinearLayout input = new LinearLayout(this);
        input.setOrientation(LinearLayout.VERTICAL);

        TextView urlLabel = new TextView(this);
        urlLabel.setText(osmApi.getUrlStart());
        input.addView(urlLabel);


        tagEditor = new TagEditor(this, osmApi.getBaseDirectory());

        input.addView(new EditTextTool(tagEditor), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));


        TextView postLabel = new TextView(this);
        postLabel.setText(osmApi.getUrlEnd());
        input.addView(postLabel);

        return input;
    }

    private MainControlBar createControlBar() {
        MainControlBar bar = new MainControlBar(this);

        download = new BusyButton(this, R.drawable.go_bottom_inverse);

        bar.addView(download);
        download.setOnClickListener(this);

        addButtons(bar);

        fileMenu = bar.addImageButton(R.drawable.edit_select_all_inverse);

        ToolTip.set(download, R.string.tt_nominatim_query);
        bar.setOnClickListener1(this);


        return bar;
    }


    public abstract OsmApiHelper createUrlGenerator(BoundingBoxE6 boundingBox) throws SecurityException, IOException;
    public abstract void addButtons(ControlBar bar);


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
                    String query = tagEditor.getText().toString();

                    BackgroundService background = getServiceContext().getBackgroundService();


                    request.stopProcessing();
                    download.startWaiting();

                    request = new ApiQueryHandle(
                            osmApi.getUrl(query),
                            osmApi.getResultFile(),
                            query,
                            osmApi.getQueryFile(),
                            getServiceContext().getContext());

                    background.process(request);

                } catch (Exception e) {
                    download.stopWaiting();
                    request = DownloadTask.NULL;

                    AppLog.e(AbsOsmApiActivity.this, e);
                }
            }
        };
    }



    private final BroadcastReceiver  onFileDownloaded = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppIntent.hasUrl(intent, request.toString())) {
                download.stopWaiting();
                request = BackgroundTask.NULL;
            }
        }
    };


    private void showFileMenu(View parent) throws IOException {
        final String query = TextBackup.read(osmApi.getQueryFile());
        final String prefix = OsmApiHelper.getFilePrefix(query);
        final String extension = osmApi.getFileExtension();

        new FileMenu(this, osmApi.getResultFile(),
                prefix, extension).showAsPopup(this, parent);
    }


    public void appendText(String s) {
        if (tagEditor.getEditableText().length()>0)
            tagEditor.append("\n");
        tagEditor.append(s);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(onFileDownloaded);
        super.onDestroy();
    }


    private static class ApiQueryHandle extends DownloadTask {
        private final String queryString;
        private final Foc queryFile;
        private final Context context;


        public ApiQueryHandle(String source, Foc target, String qs, Foc qf, Context c) {
            super(source, target);
            queryString = qs;
            queryFile   = qf;
            context = c;
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
            AppLog.e(context, e);
        }
    }
}
