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
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.background.DownloadHandle;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.TagEditor;
import ch.bailu.aat.views.preferences.AddOverlayDialog;
import ch.bailu.util_java.foc.Foc;


public abstract class AbsOsmApiActivity extends AbsDispatcher implements OnClickListener {
    //private static final String SOLID_KEY=AbsOsmApiActivity.class.getSimpleName();


    private TagEditor          tagEditor;
    private BusyButton         download;
    private View               erase;
    private View               saveCopy;
    private View               addLayer;

    private NodeListView       list;

    private OsmApiHelper       osmApi;
    private ProcessHandle      request=ProcessHandle.NULL;



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



    private LinearLayout createContentView()  {
//        private final LayoutParams layout =
//                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


        ContentView contentView = new ContentView(this);
        ControlBar bar = createControlBar();
        contentView.addView(bar);


        LinearLayout input = new LinearLayout(this);
        input.setOrientation(LinearLayout.VERTICAL);

        TextView urlLabel = new TextView(this);
        urlLabel.setText(osmApi.getUrlStart());
        input.addView(urlLabel);

        tagEditor = new TagEditor(this, osmApi.getBaseDirectory());

        input.addView(tagEditor, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));


        TextView postLabel = new TextView(this);
        postLabel.setText(osmApi.getUrlEnd());
        input.addView(postLabel);
        //AppTheme.themify(postLabel);

        list = new NodeListView(getServiceContext(), this);

        PercentageLayout percentage = new PercentageLayout(this);
        percentage.add(input, 30);
        percentage.add(list, 70);

        contentView.addView(percentage);
        return contentView;
    }


    private ControlBar createControlBar() {
        ControlBar bar = new MainControlBar(this,6);

        download = new BusyButton(this, R.drawable.go_bottom_inverse);

        bar.addView(download);
        download.setOnClickListener(this);

        erase = bar.addImageButton(R.drawable.edit_clear_all_inverse);
        addLayer = bar.addImageButton(R.drawable.view_paged_inverse);
        saveCopy = bar.addImageButton(R.drawable.document_save_as_inverse);


        ToolTip.set(download, R.string.tt_nominatim_query);
        ToolTip.set(erase, R.string.tt_nominatim_clear);
        ToolTip.set(addLayer, R.string.tt_nominatim_overlay);
        ToolTip.set(saveCopy, R.string.tt_nominatim_save);

        addButtons(bar);

        bar.setOnClickListener1(this);


        return bar;
    }


    public abstract OsmApiHelper createUrlGenerator(BoundingBoxE6 boundingBox) throws SecurityException, IOException;
    public abstract void addButtons(ControlBar bar);


    @Override
    public void onClick(View v) {
        if (v==download) {
            download();

        } else if (v==erase) {
            tagEditor.erase();

        } else if (v==saveCopy) {
            saveCopy();
        } else if (v==addLayer) {
            new AddOverlayDialog(this,osmApi.getResultFile());
        }


    }


    private void download() {
        if (getServiceContext().lock()) {
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

                background.download(request);

            } catch (Exception e) {
                download.stopWaiting();
                request = DownloadHandle.NULL;

                AppLog.e(this, e);
            }
            getServiceContext().free();
        }
    }



    private final BroadcastReceiver  onFileDownloaded = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppIntent.hasUrl(intent, request.toString())) {
                download.stopWaiting();
                request = ProcessHandle.NULL;
            }
        }
    };


    private void saveCopy() {
        try {
            final Foc source = osmApi.getResultFile();
            final Foc target = getOverlayFile();

            source.cp(target);


            AppBroadcaster.broadcast(
                    this,
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    target.getPath(),
                    source.getPath());

        } catch (IOException e) {
            AppLog.e(this, e);
        }
    }


    private Foc getOverlayFile() throws IOException {
        final String query = TextBackup.read(osmApi.getQueryFile());
        final String prefix = OsmApiHelper.getFilePrefix(query);
        final String extension = osmApi.getFileExtension();
        final Foc directory = AppDirectory.getDataDirectory(this, AppDirectory.DIR_OVERLAY);

        return AppDirectory.generateUniqueFilePath(directory, prefix, extension);
    }




    @Override
    public void onDestroy() {
        unregisterReceiver(onFileDownloaded);
        super.onDestroy();
    }


    private static class ApiQueryHandle extends DownloadHandle {
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
            AppLog.d(this, getFile().getPath());

            try {
                long size = bgDownload();
                TextBackup.write(queryFile, queryString);
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
