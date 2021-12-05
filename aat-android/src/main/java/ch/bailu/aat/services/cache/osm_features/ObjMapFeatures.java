package ch.bailu.aat.services.cache.osm_features;


import java.util.ArrayList;

import ch.bailu.aat.preferences.map.SolidOsmFeaturesList;
import ch.bailu.aat.util.filter_list.AbsFilterList;
import ch.bailu.aat.util.filter_list.KeyList;
import ch.bailu.aat.util.filter_list.ListEntry;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.OnObject;

public final class ObjMapFeatures extends Obj {

    public static final String ID_FULL = ObjMapFeatures.class.getSimpleName();
    public static final String ID_SMALL = ObjMapFeatures.class.getSimpleName()+"/s";

    private boolean isLoaded = false;
    private long size = 0;

    private class List {
        private final ArrayList<MapFeaturesListEntry> list = new ArrayList<>(50);

        public synchronized void clear() {
            list.clear();
        }

        public synchronized void sync(AbsFilterList<ListEntry> f) {
            for (int i = f.sizeAll(); i < list.size(); i++) {
                f.add(list.get(i));
            }
        }

        public synchronized void add(MapFeaturesListEntry d) {
            list.add(d);
        }
    }

    private final List list = new List();


    public ObjMapFeatures(String ID) {
        super(ID);
    }


    @Override
    public void onInsert(AppContext sc) {
        super.onInsert(sc);

        sc.getServices().getBackgroundService().process(new ListLoader(getID()));
    }

    @Override
    public void onDownloaded(String id, String url, AppContext sc) {}

    @Override
    public void onChanged(String id, AppContext sc) {}

    @Override
    public long getSize() {
        return size;
    }


    public void syncList(AbsFilterList<ListEntry> filterList) {
        list.sync(filterList);
    }


    @Override
    public boolean isReadyAndLoaded() {
        return isLoaded;
    }


    public static class ListLoader extends BackgroundTask implements MapFeaturesParser.OnHaveFeature {
        private final String ID;

        private AppContext appContext;

        private ObjMapFeatures owner = null;
        public ListLoader(String id) {
            ID = id;
        }

        public KeyList keyList;

        private int doBroadcast = 0;

        @Override
        public long bgOnProcess(final AppContext appContext) {
            ListLoader.this.appContext = appContext;
            final long[] size = {0};
            new OnObject(appContext, ID, ObjMapFeatures.class) {
                @Override
                public void run(Obj handle) {
                    owner = (ObjMapFeatures) handle;

                    parseMapFeatures();

                    owner.isLoaded = true;
                    ListLoader.this.appContext.getBroadcaster().broadcast(
                            AppBroadcaster.FILE_CHANGED_INCACHE, ID);

                    size[0] = owner.size;
                    owner = null;
                    ListLoader.this.appContext = null;
                }
            };
            return size[0];
        }


        private void parseMapFeatures() {
            keyList = SolidOsmFeaturesList.getKeyList(ID);
            new MapFeaturesParser(appContext.getAssets(), this);
        }


        @Override
        public boolean onParseFile(String file) {
            return keyList.isEmpty() || keyList.hasKey(file.toLowerCase());

        }

        @Override
        public void onHaveFeature(MapFeaturesParser parser) {
            MapFeaturesListEntry d = new MapFeaturesListEntry(parser);

            owner.size += d.length() * 2;
            owner.list.add(d);

            doBroadcast++;
            if (doBroadcast > 10) {
                doBroadcast = 0;
                appContext.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, ID);
            }
        }
    }

    public static class Factory extends Obj.Factory {
        final String ID;

        public Factory(String id) {
            ID = id;
        }

        @Override
        public Obj factory(String id, AppContext cs) {
            return new ObjMapFeatures(ID);
        }
    }
}
