package ch.bailu.aat.views.osm_features;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.filter_list.FilterList;
import ch.bailu.aat.util.filter_list.PoiListEntry;
import ch.bailu.aat.util.ui.AppTheme;

public class PoiListView extends ListView {


    private DataSetObserver observer=null;
    private final FilterList list;
    private final ServiceContext scontext;

    private OnSelected onSelected = OnSelected.NULL;


    public PoiListView(ServiceContext sc, FilterList l) {
        super(sc.getContext());

        scontext = sc;
        list = l;
        final PoiListView.Adapter listAdapter = new PoiListView.Adapter();

        AppTheme.themifyList(this);

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
            PoiListEntryView view;
            if (v instanceof  MapFeaturesEntryView) {
                view = (PoiListEntryView) v;
            } else {
                view = new PoiListEntryView(scontext, onSelected);
            }

            view.set((PoiListEntry) list.get(index));
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
            onSelected.onSelected(list.get(index), 0,null);
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return list.get(position).getID();
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
