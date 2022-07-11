package ch.bailu.aat_lib.dispatcher;


import ch.bailu.aat_lib.service.cache.ObjGpx;
import ch.bailu.aat_lib.service.cache.ObjGpxStatic;
import ch.bailu.aat_lib.service.directory.Iterator;
import ch.bailu.aat_lib.service.directory.Iterator.OnCursorChangedListener;
import ch.bailu.aat_lib.service.directory.IteratorFollowFile;
import ch.bailu.aat_lib.service.directory.IteratorSummary;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjNull;

public abstract class IteratorSource extends ContentSource implements OnCursorChangedListener {

    private final AppContext appContext;
    private final SolidDirectoryQuery sdirectory;

    private Iterator iterator = Iterator.NULL;



    @Override
    public void onCursorChanged() {
        requestUpdate();
    }


    public IteratorSource(AppContext appContext) {
        this.appContext = appContext;
        sdirectory = new SolidDirectoryQuery(appContext.getStorage(), appContext);
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
        iterator = factoryIterator(appContext);
        iterator.moveToPosition(sdirectory.getPosition().getValue());
        iterator.setOnCursorChangedListener(this);
    }


    public abstract Iterator factoryIterator(AppContext appContext);

    @Override
    public GpxInformation getInfo() {
        return iterator.getInfo();
    }

    public void moveToPrevious() {
        if (!iterator.moveToPrevious())
            iterator.moveToPosition(iterator.getCount()-1);

        sdirectory.getPosition().setValue(iterator.getPosition());
        requestUpdate();
    }

    public void moveToNext() {
        if (!iterator.moveToNext())
            iterator.moveToPosition(0);

        sdirectory.getPosition().setValue(iterator.getPosition());
        requestUpdate();
    }




    public static class FollowFile extends IteratorSource {
        private final AppContext appContext;
        private Obj handle = ObjNull.NULL;

        public FollowFile(AppContext appContext) {
            super(appContext);
            this.appContext = appContext;
        }

        @Override
        public Iterator factoryIterator(AppContext appContext) {
            return new IteratorFollowFile(appContext);
        }


        private final BroadcastReceiver onChangedInCache = objs -> {
            if (getID().equals(objs[0])) {
                requestUpdate();
            }

        };

        @Override
        public void onPause() {
            appContext.getBroadcaster().unregister(onChangedInCache);

            handle.free();
            handle = ObjNull.NULL;
            super.onPause();
        }


        @Override
        public void onResume() {
            appContext.getBroadcaster().register(onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
            super.onResume();
        }

        @Override
        public GpxInformation getInfo() {
            final GpxInformation[] info = {super.getInfo()};

            new InsideContext(appContext.getServices()) {
                @Override
                public void run() {
                    Obj h = appContext.getServices().getCacheService().getObject(getID(),
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
            return super.getInfo().getFile().toString();
        }

    }


    public static class Summary extends IteratorSource {
        public Summary(AppContext appContext) {
            super(appContext);
        }

        @Override
        public Iterator factoryIterator(AppContext appContext) {
            return new IteratorSummary(appContext);
        }
    }
}
