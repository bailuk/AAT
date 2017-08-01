package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.Iterator.OnCursorChangedListener;
import ch.bailu.aat.services.directory.IteratorFollowFile;
import ch.bailu.aat.services.directory.IteratorSummary;

public abstract class IteratorSource extends ContentSource implements OnCursorChangedListener {

    private final ServiceContext scontext;
    private final SolidDirectoryQuery sdirectory;

    private Iterator iterator = Iterator.NULL;



    @Override
    public void onCursorChanged() {
        requestUpdate();
    }


    public IteratorSource(ServiceContext sc) {
        scontext = sc;
        sdirectory = new SolidDirectoryQuery(sc.getContext());
    }



    @Override
    public void requestUpdate() {
        sendUpdate(iterator.getInfoID(), getInfo());
    }




    @Override
    public void onPause() {
        iterator.close();
        iterator = Iterator.NULL;
    }


    @Override
    public void onResume() {
        iterator = factoryIterator(scontext);
        iterator.moveToPosition(sdirectory.getPosition().getValue());
        iterator.setOnCursorChangedLinsener(this);
    }


    public abstract Iterator factoryIterator(ServiceContext scontext);

    public GpxInformation getInfo() {
        return iterator.getInfo();
    }

    public void moveToPrevious() {
        if (iterator.moveToPrevious() == false)
            iterator.moveToPosition(iterator.getCount()-1);

        sdirectory.getPosition().setValue(iterator.getPosition());
        requestUpdate();
    }

    public void moveToNext() {
        if (iterator.moveToNext() == false)
            iterator.moveToPosition(0);

        sdirectory.getPosition().setValue(iterator.getPosition());
        requestUpdate();
    }




    public static class FollowFile extends IteratorSource {
        private final Context context;
        private final ServiceContext scontext;
        private ObjectHandle handle = ObjectHandle.NULL;


        public FollowFile(ServiceContext sc) {
            super(sc);
            context = sc.getContext();
            scontext = sc;
        }

        @Override
        public Iterator factoryIterator(ServiceContext scontext) {
            return new IteratorFollowFile(scontext);
        }


        private final BroadcastReceiver  onChangedInCache = new BroadcastReceiver () {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppIntent.hasFile(intent, getIID())) {
                    requestUpdate();
                }
            }
        };

        @Override
        public void onPause() {
            context.unregisterReceiver(onChangedInCache);

            handle.free();
            handle = ObjectHandle.NULL;
            super.onPause();
        }


        @Override
        public void onResume() {
            AppBroadcaster.register(context, onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
            super.onResume();
        }

        @Override
        public GpxInformation getInfo() {
            GpxInformation info = super.getInfo();

            if (scontext.lock()) {
                ObjectHandle h = scontext.getCacheService().getObject(getIID(),
                        new GpxObjectStatic.Factory());

                if (h instanceof GpxObject && h.isReadyAndLoaded()) {
                    handle.free();
                    handle = h;

                    info = new GpxFileWrapper(h.getFile(), ((GpxObject)h).getGpxList());
                } else {
                    h.free();
                }

                scontext.free();
            }
            return info;
        }

        private String getIID() {
            return super.getInfo().getFile().getPath();
        }

    }


    public static class Summary extends IteratorSource {
        public Summary(ServiceContext sc) {
            super(sc);
        }

        @Override
        public Iterator factoryIterator(ServiceContext scontext) {
            return new IteratorSummary(scontext);
        }


    }

}
