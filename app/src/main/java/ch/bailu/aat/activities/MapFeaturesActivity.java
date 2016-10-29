package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDialog;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.FeaturesList;
import ch.bailu.aat.views.MainControlBar;


public class MapFeaturesActivity extends AbsDispatcher implements OnClickListener {
    
    private FeaturesList list;
    
    private BusyButton download;

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ContentView contentView = new ContentView(this, ContentView.VERTICAL);
        
        download = new BusyButton(this,R.drawable.go_bottom_inverse);
        ToolTip.set(download, R.string.tt_overpass_download_features);
        download.setOnClickListener(this);

        final ControlBar bar = new MainControlBar(getServiceContext(), LinearLayout.HORIZONTAL, 6);
        
        bar.addView(download);
        bar.addIgnoreSize(AppTheme.getTitleTextView(this, R.string.query_features));

        list = new FeaturesList(this);

        contentView.addView(bar);
        contentView.addView(list);

        setContentView(contentView);

        
        AppBroadcaster.register(this, onFileProcessed, AppBroadcaster.FILE_CHANGED_ONDISK);

    }
    

    @Override
    public void onDestroy() {
        unregisterReceiver(onFileProcessed);
        super.onDestroy();
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        loadList();
    }


    @Override
    public void onClick(View v) {
        if (v==download) {
            new AppDialog() {
                
                @Override
                protected void onPositiveClick() {
                    getServiceContext().getBackgroundService().downloadMapFeatures();
                    download.startWaiting();        
                }
                
            }.displayYesNoDialog(MapFeaturesActivity.this, 
                    getString(R.string.query_features), 
                    getString(R.string.query_download_features));

            
            
        }
    }



    private final BroadcastReceiver onFileProcessed = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (AppIntent.hasFile(intent, AppDirectory.getMapFeatureIndex(context).toString())) {
                    download.stopWaiting();
                    loadList();                }
            } catch (Exception e) {
                loadList();
                download.stopWaiting();
                AppLog.e(MapFeaturesActivity.this, this,e);
            }
        }
    };


    private void loadList() {
        if (getServiceContext().isUp())
            list.loadList(getServiceContext().getIconMapService());
    }

}
