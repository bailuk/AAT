package ch.bailu.aat.views.tileremover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ControlBar;


public class TileRemoverView
        extends LinearLayout
        implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private TilesSummaryView summaryView;
    private BusyButton scan, remove;

    private final SolidTileCacheDirectory sdirectory;

    private final ServiceContext scontext;



    public TileRemoverView(ServiceContext sc) {
        super(sc.getContext());

        setOrientation(HORIZONTAL);
        scontext=sc;

        sdirectory = new SolidTileCacheDirectory(getContext());

        addW(createFilterLayout(getContext()));
        addView(createControlBar(getContext()));

    }

    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }

    private View createFilterLayout(Context context) {
        summaryView = new TilesSummaryView(context);
        return summaryView;
    }


    private View createControlBar(Context context) {
        final ControlBar bar =  new ControlBar(context, LinearLayout.VERTICAL);

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
            updateText();
        }
    };



    private final BroadcastReceiver onTileRemoverRemove = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            remove.startWaiting();
            scan.stopWaiting();
            updateText();
        }
    };

    private final BroadcastReceiver onTileRemoverScan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scan.startWaiting();
            remove.stopWaiting();
            updateText();
        }
    };


    public void updateText() {
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
                tr.getState().resetAndRescan();
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
