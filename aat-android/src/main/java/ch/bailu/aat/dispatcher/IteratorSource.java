package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.preferences.SolidDirectoryQuery;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.services.cache.ObjGpx;
import ch.bailu.aat.services.cache.ObjGpxStatic;
import ch.bailu.aat.services.cache.ObjNull;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.Iterator.OnCursorChangedListener;
import ch.bailu.aat.services.directory.IteratorFollowFile;
import ch.bailu.aat.services.directory.IteratorSummary;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;

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
        sdirectory = new SolidDirectoryQuery(new Storage(sc.getContext()), new AndroidFocFactory(sc.getContext()));
    }



    @Override
    public void requestUpdate() {
        sendUpdate(iterator.getInfoID(), getInfo());
    }





    @Override
    public int getIID() {
        return iterator.getInfoID();
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

    @Override
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
        private Obj handle = ObjNull.NULL;


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
                if (AppIntent.hasFile(intent, getID())) {
                    requestUpdate();
                }
            }
        };

        @Override
        public void onPause() {
            context.unregisterReceiver(onChangedInCache);

            handle.free();
            handle = ObjNull.NULL;
            super.onPause();
        }


        @Override
        public void onResume() {
            OldAppBroadcaster.register(context, onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
            super.onResume();
        }

        @Override
        public GpxInformation getInfo() {
            final GpxInformation[] info = {super.getInfo()};

            new InsideContext(scontext) {
                @Override
                public void run() {
                    Obj h = scontext.getCacheService().getObject(getID(),
                            new ObjGpxStatic.Factory());

                    if (h instanceof ObjGpx) {
                        handle.free();
                        handle = h;

                        if (handle.isReadyAndLoaded())
                            info[0] = new GpxFileWrapper(handle.getFile(),
                                    ((ObjGpx)handle).getGpxList());
                    } else {

                        h.free();
                    }

                }
            };
            return info[0];
        }

        private String getID() {
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
