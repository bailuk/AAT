package ch.bailu.aat.views.tileremover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.preferences.SolidTrimDate;
import ch.bailu.aat.preferences.SolidTrimMode;
import ch.bailu.aat.preferences.SolidTrimSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;


public class TileRemoverContentView
        extends ContentView
        implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private SolidIndexListView sdirectoryView;
    private TilesSummaryView summaryView;
    private SolidIndexListView strimsizeView;
    private SolidIndexListView strimdateView;



    private BusyButton scan, remove;

    private final SolidTileCacheDirectory sdirectory;

    private final ServiceContext scontext;



    public TileRemoverContentView(ServiceContext sc) {
        super(sc.getContext(), HORIZONTAL);

        scontext=sc;

        sdirectory = new SolidTileCacheDirectory(getContext());

        addView(createControlBar(getContext()));
        addView(createFilterLayout(getContext()));
    }


    private View createFilterLayout(Context context) {
        final ScrollView scrollView = new ScrollView(context);
        final LinearLayout filter = new LinearLayout(context);

        filter.setOrientation(LinearLayout.VERTICAL);

        sdirectoryView = new SolidIndexListView(context, sdirectory);
        filter.addView(sdirectoryView);

        summaryView = new TilesSummaryView(context);
        filter.addView(summaryView);

        filter.addView(new SolidIndexListView(context, new SolidTrimMode(context)));

        strimsizeView = new SolidIndexListView(context, new SolidTrimSize(context));
        filter.addView(strimsizeView);

        strimdateView = new SolidIndexListView(context, new SolidTrimDate(context));
        filter.addView(strimdateView);


        scrollView.addView(filter);
        return scrollView;
    }


    private View createControlBar(Context context) {
        final ControlBar bar =  new ControlBar(context, LinearLayout.VERTICAL);

        bar.addImageButton(R.drawable.folder_inverse);


        scan = new BusyButton(context, R.drawable.view_refresh_inverse);
        bar.addButton(scan);

        remove = new BusyButton(context, R.drawable.user_trash_inverse);
        bar.addButton(remove);

        bar.setOnClickListener1(this);
        return bar;
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        sdirectory.register(this);

        AppBroadcaster.register(getContext(),
                onTileRemoverStopped,
                AppBroadcaster.TILE_REMOVER_STOPPED);


        AppBroadcaster.register(getContext(),
                onTileRemoverScan,
                AppBroadcaster.TILE_REMOVER_SCAN);

        AppBroadcaster.register(getContext(),
                onTileRemoverRemove,
                AppBroadcaster.TILE_REMOVER_REMOVE);

    }


    private final BroadcastReceiver onTileRemoverStopped = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scan.stopWaiting();
            remove.stopWaiting();
            updateInfo();
        }
    };



    private final BroadcastReceiver onTileRemoverRemove = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            remove.startWaiting();
            scan.stopWaiting();
            updateInfo();
        }
    };

    private final BroadcastReceiver onTileRemoverScan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scan.startWaiting();
            remove.stopWaiting();
            updateInfo();
        }
    };


    public void updateInfo() {
        final TileRemoverService tr = scontext.getTileRemoverService();
        if (tr != null) {
            summaryView.updateInfo(tr.getSummaries());
        }
    }

    @Override
    public void onClick(View v) {
        final TileRemoverService tr = scontext.getTileRemoverService();
        if (tr != null) {
            if (v == scan && scan.isWaiting()) {
                tr.getState().stop();
            } else if (v == scan) {
                tr.getState().scan();
            } else if (v == remove && remove.isWaiting()) {
                tr.getState().stop();
            } else if (v == remove) {
                tr.getState().remove();
            }
        }
    }





    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final TileRemoverService tr = scontext.getTileRemoverService();
        if (tr != null) {
            if(sdirectory.hasKey(key)) {
                tr.getState().reset();
            } else if (key.contains("SolidTrim")) {
                tr.getState().rescan();
            }

        }


    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sdirectory.unregister(this);

        getContext().unregisterReceiver(onTileRemoverStopped);
        getContext().unregisterReceiver(onTileRemoverScan);
        getContext().unregisterReceiver(onTileRemoverRemove);

    }

}
