package ch.bailu.aat.views.osm_features;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.lib.filter_list.FilterList;
import ch.bailu.aat_lib.search.poi.PoiListItem;

public class PoiListView extends ListView {

    private DataSetObserver observer=null;
    private final FilterList list;

    private OnSelected onSelected = OnSelected.NULL;

    private final UiTheme theme;

    public PoiListView(Context context, FilterList l, UiTheme theme) {
        super(context);

        this.theme = theme;

        list = l;
        final PoiListView.Adapter listAdapter = new PoiListView.Adapter();

        AppTheme.search.list(this);

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
            if (v instanceof PoiListEntryView) {
                view = (PoiListEntryView) v;
            } else {
                view = new PoiListEntryView(PoiListView.this.getContext(), onSelected, theme);
            }

            view.set((PoiListItem) list.getFromVisible(index));
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
            onSelected.onSelected(list.getFromVisible(index), 0,null);
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
