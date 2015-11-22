package ch.bailu.aat.services.srtm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;


public class ElevationUpdater implements CleanUp{
    private final SparseArray <ElevationUpdaterEntry> pendingFiles = new SparseArray<ElevationUpdaterEntry>();
    private final CacheService cache;
    private final BackgroundService background;
    private final Context context;


    private SrtmAccess srtmAccess=SrtmAccess.NULL_READY;

    protected ElevationUpdater(CacheService c, BackgroundService b) {
        cache = c;
        context = c;
        background = b;

        AppBroadcaster.register(c, onRequestElevationUpdate, AppBroadcaster.REQUEST_ELEVATION_UPDATE);
        AppBroadcaster.register(c, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
    }



    private BroadcastReceiver onRequestElevationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppBroadcaster.getFile(intent);
            pendingFiles.put(id.hashCode(), new ElevationUpdaterEntry(cache, id));
            state.ping();
        }
    };



    private BroadcastReceiver onFileChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppBroadcaster.getFile(intent);

            if (id.contains(srtmAccess.toString()) || pendingFiles.get(id.hashCode()) != null) {
                state.ping();
            }
        }
    };


    private State state = new StateNextTile();

    private abstract class State {
        public abstract void ping();
    }

    private class StateNextTile extends State {

        @Override
        public void ping() {
            for (int i = pendingFiles.size()-1; i>-1; i--) {
                final SrtmCoordinates c = pendingFiles.valueAt(i).getNextSRTMTile();
                if (c!=null)  {
                    changeSRTM(c);


                    state = new StateWaitTile();
                    state.ping();

                    return;
                }
            }            
        }

        private void changeSRTM(SrtmCoordinates c) {
            if (srtmAccess.hashCode()!=c.hashCode()) {
                srtmAccess.cleanUp();
                srtmAccess=new Srtmgl3TileAccess(c, cache);
            }
        }

    }


    private class StateWaitTile extends State {

        @Override
        public void ping() {
            if (srtmAccess.isReady()) {

                for (int i = pendingFiles.size()-1; i>-1; i--) {
                    pendingFiles.valueAt(i).update(background, srtmAccess);
                    if (pendingFiles.valueAt(i).getNextSRTMTile() == null) {
                        pendingFiles.remove(pendingFiles.keyAt(i));
                    }

                }
                state = new StateUpdate();
                state.ping();

            }
        }

    }


    private class StateUpdate extends State {

        @Override
        public void ping() {
            if (isUpdating() == false) {
                state = new StateNextTile();
                state.ping();

            }
        }

        private boolean isUpdating() {
            for (int i = 0; i< pendingFiles.size(); i++) {
                if (pendingFiles.valueAt(i).isUpdating()) return true;
            }
            return false;
        }

    }



    @Override
    public void cleanUp() {

        context.unregisterReceiver(onRequestElevationUpdate);
        context.unregisterReceiver(onFileChanged);
        srtmAccess.cleanUp();

    }

}
