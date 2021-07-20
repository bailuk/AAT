package ch.bailu.aat.views.tileremover;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.menus.RemoveTilesMenu;
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.BusyViewControl;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory;


public class TileRemoverView
        extends LinearLayout
        implements View.OnClickListener, OnPreferencesChanged {


    private TileSummariesView summaryView;
    private ImageButtonViewGroup scan, remove;
    private BusyViewControl bscan, bremove;

    private final SolidTileCacheDirectory sdirectory;

    private final ServiceContext scontext;
    private final Activity acontext;



    public TileRemoverView(ServiceContext sc, Activity ac, UiTheme theme) {
        super(sc.getContext());

        setOrientation(HORIZONTAL);
        scontext = sc;
        acontext = ac;

        sdirectory = new AndroidSolidTileCacheDirectory(getContext());

        addW(createFilterLayout(ac, theme));
        addView(createControlBar(getContext(), theme));


    }

    public void addW(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }

    private View createFilterLayout(Activity acontext, UiTheme theme) {
        summaryView = new TileSummariesView(acontext, theme);
        return summaryView;
    }


    private View createControlBar(Context context, UiTheme theme) {
        final ControlBar bar =  new ControlBar(context, LinearLayout.VERTICAL, AppTheme.bar);

        scan = new ImageButtonViewGroup(context, R.drawable.view_refresh_inverse);
        bscan = new BusyViewControl(scan);
        bar.addButton(scan);

        remove = new ImageButtonViewGroup(context, R.drawable.user_trash_inverse);
        bremove = new BusyViewControl(remove);
        bar.addButton(remove);

        bar.setOnClickListener1(this);

        theme.button(scan);
        theme.button(remove);
        theme.background(bar);

        return bar;
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        sdirectory.register(this);

        OldAppBroadcaster.register(getContext(),
                onTileRemoverStopped,
                AppBroadcaster.TILE_REMOVER_STOPPED);


        OldAppBroadcaster.register(getContext(),
                onTileRemoverScan,
                AppBroadcaster.TILE_REMOVER_SCAN);

        OldAppBroadcaster.register(getContext(),
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
    public void onPreferencesChanged(StorageInterface s, String key) {

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
