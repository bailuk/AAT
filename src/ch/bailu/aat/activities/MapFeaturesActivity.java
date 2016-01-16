package ch.bailu.aat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.DownloadButton;
import ch.bailu.aat.views.FeaturesList;


public class MapFeaturesActivity extends AbsDispatcher implements OnClickListener {
    
    private static final Class<?> SERVICES[] = {
        BackgroundService.class
    };    

    private FeaturesList list;
    
    private ControlBar bar;
    private DownloadButton download;

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ContentView contentView = new ContentView(this, ContentView.VERTICAL);
        
        download = new DownloadButton(this);
        ToolTip.set(download, R.string.tt_overpass_download_features);
        
        bar = new ControlBar(this,ControlBar.HORIZONTAL,6);
        
        bar.setOnClickListener2(this);
        bar.addView(download);
        
        
        TextView text = new TextView(this);
        text.setText(R.string.query_features);
        text.setTextColor(AppTheme.getHighlightColor());
        text.setTextSize(25);
        bar.addViewIgnoreSize(text);

        list = new FeaturesList(this,null);
        list.loadList();
        
        contentView.addView(bar);
        contentView.addView(list);

        setContentView(contentView);

        connectToServices(SERVICES);
        
        AppBroadcaster.register(this, onFileProcessed, AppBroadcaster.FILE_CHANGED_ONDISK);

    }
    
    @Override
    public void onServicesUp() {
        
    }

    
    @Override
    public void onDestroy() {
        unregisterReceiver(onFileProcessed);
        super.onDestroy();
    }
    
    
    @Override
    public void onClick(View v) {
        if (v==download) {
            try {
                getBackgroundService().downloadMapFeatures();
                download.startWaiting();
            } catch (ServiceNotUpException e) {
                AppLog.e(this, this, e);
            }
        }
    }
    
    private BroadcastReceiver onFileProcessed = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (AppBroadcaster.hasFile(intent, AppDirectory.getMapFeatureIndex(context).toString())) {
                    download.stopWaiting();
                    list.loadList();
                }
            } catch (Exception e) {
                download.stopWaiting();
                list.loadList();
                AppLog.e(MapFeaturesActivity.this, this,e);
            }
        }
    };
}
