package ch.bailu.aat.services.cache.osm_features;


import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.OnObject;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.filter_list.FilterList;
import ch.bailu.aat.util.fs.foc.FocAsset;

public class FeaturesListObject extends ObjectHandle {

    private static final long SIZE = 1024*1024;
    public static final String ID = FeaturesListObject.class.getSimpleName();






    private class List {
        private final ArrayList<ListData> list = new ArrayList<>(50);

        public synchronized void clear() {
            list.clear();
        }

        public synchronized void sync(FilterList<ListData> f) {
            for (int i = f.size(); i < list.size(); i++) {
                f.add(list.get(i));
            }
        }

        public synchronized void add(ListData d) {
            list.add(d);
        }
    }

    private final List list = new List();


    public FeaturesListObject() {
        super(ID);
    }


    @Override
    public void onInsert(ServiceContext sc) {
        super.onInsert(sc);

        sc.getBackgroundService().process(new ListLoader());
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}

    @Override
    public void onChanged(String id, ServiceContext sc) {}

    @Override
    public long getSize() {
        return SIZE;
    }


    public void syncList(FilterList<ListData> filterList) {
        list.sync(filterList);
    }


    public static class ListLoader extends BackgroundTask implements MapFeaturesParser.OnHaveFeature {
        private static final String MAP_FEATURES_ASSET = "map_features";

        private Icon icons;
        private ServiceContext scontext;

        private FeaturesListObject owner = null;

        @Override
        public long bgOnProcess(final ServiceContext sc) {
            new OnObject(sc, ID, FeaturesListObject.class) {
                @Override
                public void run(ObjectHandle handle) {
                    owner = (FeaturesListObject) handle;
                    scontext = sc;
                    bgOnProcess(sc.getContext());

                    AppBroadcaster.broadcast(sc.getContext(),
                            AppBroadcaster.FILE_CHANGED_INCACHE, ID);

                    owner = null;
                    scontext = null;
                }
            };
            return SIZE;
        }


        private void bgOnProcess(Context context) {
            icons = new Icon(context);

            AssetManager assets = context.getAssets();
            ArrayList<String> files = FocAsset.listAssets(assets, MAP_FEATURES_ASSET);

            Collections.sort(files);
            try {
                new MapFeaturesParser(assets, this, files);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onHaveFeature(MapFeaturesParser parser) {
            ListData d = new ListData(scontext, parser, icons);
            owner.list.add(d);
        }
    }


    public static class Factory extends ObjectHandle.Factory {
        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new FeaturesListObject();
        }
    }
}
