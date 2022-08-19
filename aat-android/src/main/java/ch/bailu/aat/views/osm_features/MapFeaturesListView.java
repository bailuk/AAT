package ch.bailu.aat.views.osm_features;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.osm_features.MapFeaturesListEntry;
import ch.bailu.aat_lib.lib.filter_list.AbsFilterList;
import ch.bailu.aat_lib.lib.filter_list.ListEntry;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;


public class MapFeaturesListView extends ListView  {

    private DataSetObserver observer=null;
    private final AbsFilterList<ListEntry> list;
    private final ServiceContext scontext;

    private OnSelected onSelected = OnSelected.NULL;

    private final static UiTheme theme = AppTheme.search;


    public MapFeaturesListView(ServiceContext sc, AbsFilterList<ListEntry> l) {
        super(sc.getContext());

        scontext = sc;
        list = l;
        final Adapter listAdapter = new Adapter();

        theme.list(this);

        setAdapter(listAdapter);
        setOnItemClickListener(listAdapter);
    }


    public void onChanged() {
        if (observer != null) observer.onChanged();
    }


    public void setOnTextSelected(OnSelected s) {
        onSelected = s;
    }


    private class Adapter implements ListAdapter, android.widget.AdapterView.OnItemClickListener{

        @Override
        public int getCount() {
            return list.sizeVisible();
        }


        @Override
        public View getView(int index, View v, ViewGroup p) {
            MapFeaturesEntryView view;
            if (v instanceof  MapFeaturesEntryView) {
                view = (MapFeaturesEntryView) v;
            } else {
                view = new MapFeaturesEntryView(scontext, onSelected, theme);
            }

            view.set((MapFeaturesListEntry) list.getFromVisible(index));
            return view;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver o) {
            observer=o;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver o) {
            observer = null;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
            final MapFeaturesListEntry d = (MapFeaturesListEntry) list.getFromVisible(index);

            if (d.isSummary())
                onSelected.onSelected(d,OnSelected.FILTER, d.getSummaryKey());
            else
                onSelected.onSelected(d,OnSelected.EDIT, d.getDefaultQuery());
        }

        @Override
        public Object getItem(int position) {
            return list.getFromVisible(position);
        }


        @Override
        public long getItemId(int position) {
            return list.getFromVisible(position).getID();
        }


        @Override
        public int getItemViewType(int position) {
            return 0;
        }


        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return getCount()==0;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int index) {
            return true;
        }

    }
}
