package ch.bailu.aat.views.tileremover;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.RemoveTilesMenu;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.preferences.map.SolidTileCacheDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.views.BusyViewControl;
import ch.bailu.aat.views.MyImageButton;
import ch.bailu.aat.views.bar.ControlBar;


public class TileRemoverView
        extends LinearLayout
        implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private TileSummariesView summaryView;
    private MyImageButton scan, remove;
    private BusyViewControl bscan, bremove;

    private final SolidTileCacheDirectory sdirectory;

    private final ServiceContext scontext;
    private final Activity acontext;



    public TileRemoverView(ServiceContext sc, Activity ac) {
        super(sc.getContext());

        setOrientation(HORIZONTAL);
        scontext = sc;
        acontext = ac;

        sdirectory = new SolidTileCacheDirectory(getContext());

        addW(createFilterLayout(ac));
        addView(createControlBar(getContext()));

    }

    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }

    private View createFilterLayout(Activity acontext) {
        summaryView = new TileSummariesView(acontext);
        return summaryView;
    }


    private View createControlBar(Context context) {
        final ControlBar bar =  new ControlBar(context, LinearLayout.VERTICAL);

        scan = new MyImageButton(context, R.drawable.view_refresh_inverse);
        bscan = new BusyViewControl(scan);
        bar.addButton(scan);

        remove = new MyImageButton(context, R.drawable.user_trash_inverse);
        bremove = new BusyViewControl(remove);
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
            bscan.stopWaiting();
            bremove.stopWaiting();
            updateText();
        }
    };



    private final BroadcastReceiver onTileRemoverRemove = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bremove.startWaiting();
            bscan.stopWaiting();
            updateText();
        }
    };

    private final BroadcastReceiver onTileRemoverScan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bscan.startWaiting();
            bremove.stopWaiting();
            updateText();
        }
    };


    public void updateText() {
        new InsideContext(scontext) {
            @Override
            public void run() {
                final TileRemoverService tr = scontext.getTileRemoverService();
                summaryView.updateInfo(tr.getSummaries());
            }
        };
    }

    @Override
    public void onClick(final View v) {
        new InsideContext(scontext) {
            @Override
            public void run() {

                final TileRemoverService tr = scontext.getTileRemoverService();
                if (v == scan && bscan.isWaiting()) {
                    tr.getState().stop();
                } else if (v == scan) {
                    tr.getState().scan();
                } else if (v == remove && bremove.isWaiting()) {
                    tr.getState().stop();
                } else if (v == remove) { // show menu
                    new RemoveTilesMenu(scontext, acontext).showAsDialog(scontext.getContext());
                }

            }
        };
    }





    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, final String key) {
        new InsideContext(scontext) {
            @Override
            public void run() {
                final TileRemoverService tr = scontext.getTileRemoverService();
                if (sdirectory.hasKey(key)) {
                    tr.getState().reset();
                } else if (key.contains("SolidTrim")) {
                    tr.getState().rescan();
                }

            }
        };
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
