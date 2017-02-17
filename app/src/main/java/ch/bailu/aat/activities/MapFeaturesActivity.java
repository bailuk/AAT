package ch.bailu.aat.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.FeaturesList;
import ch.bailu.aat.views.MainControlBar;


public class MapFeaturesActivity extends AbsDispatcher {
    
    private FeaturesList list;
    
//    private BusyButton download;

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ContentView contentView = new ContentView(this, ContentView.VERTICAL);
        
//        download = new BusyButton(this,R.drawable.go_bottom_inverse);
//        ToolTip.set(download, R.string.tt_overpass_download_features);
//        download.setOnClickListener(this);

        final ControlBar bar = new MainControlBar(this, LinearLayout.HORIZONTAL, 6);
        
//        bar.addView(download);
        bar.addIgnoreSize(AppTheme.getTitleTextView(this, R.string.query_features));

        list = new FeaturesList(this);

        contentView.addView(bar);
        contentView.addView(list);

        setContentView(contentView);

        //AppBroadcaster.register(this, onFileProcessed, AppBroadcaster.FILE_CHANGED_ONDISK);

    }
    

    @Override
    public void onDestroy() {
        //unregisterReceiver(onFileProcessed);
        super.onDestroy();
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        loadList();
    }



//
//    private final BroadcastReceiver onFileProcessed = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                if (AppIntent.hasFile(intent, AppDirectory.getMapFeatureIndex(context).toString())) {
//                    download.stopWaiting();
//                    loadList();                }
//            } catch (Exception e) {
//                loadList();
//                download.stopWaiting();
//                AppLog.e(MapFeaturesActivity.this, this,e);
//            }
//        }
//    };


    private void loadList() {
        if (getServiceContext().lock()) {
            list.loadList(
                    getServiceContext().getContext().getAssets(),
                    getServiceContext().getIconMapService());
            getServiceContext().free();
        }
    }

}
