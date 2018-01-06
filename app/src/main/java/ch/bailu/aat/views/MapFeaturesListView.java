package ch.bailu.aat.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ch.bailu.aat.services.cache.osm_features.ListData;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.filter_list.FilterList;
import ch.bailu.aat.util.ui.AppTheme;


public class MapFeaturesListView extends ListView  {

    private DataSetObserver observer=null;
    private final FilterList<ListData> list;

    public MapFeaturesListView(Context context, FilterList<ListData> l) {
        super(context);

        list = l;
        final Adapter listAdapter = new Adapter();


        AppTheme.themify(this, AppTheme.getHighlightColor());

        setAdapter(listAdapter);
        setOnItemClickListener(listAdapter);
    }


    public void onChanged() {
        if (observer != null) observer.onChanged();
    }


    private void broadcastKeyValue(CharSequence key, CharSequence value) {
        String text = "["+key+"="+value+"]";
        AppBroadcaster.broadcast(getContext(), AppBroadcaster.SELECT_MAP_FEATURE, text);
    }

    private class Adapter implements ListAdapter, android.widget.AdapterView.OnItemClickListener{

        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public View getView(int index, View v, ViewGroup p) {
            TextView text = (TextView) v;
            if (text==null) {
                text = new TextView(getContext());
            }
            text.setText(list.get(index).paragraph);
            return text;
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
        public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
            ListData d = list.get(index);

              if (d.key.length()>1
                    && d.value.length()>1) {
                broadcastKeyValue(d.key, d.value);
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return 0;
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
            return false;
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
