package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.IteratorSimple;

public class GpxListView extends ListView {


    private DataSetObserver observer;

    private final ContentDescription data[];


    public GpxListView(Context c, 
            ContentDescription cd[]) {
        super(c);

        data = cd;


        AppTheme.themify(this, AppTheme.getHighlightColor());
        AppBroadcaster.register(getContext(), onCursorChanged, AppBroadcaster.DB_CURSOR_CHANGED);

    }


    @Override
    public void onDetachedFromWindow() {
        getContext().unregisterReceiver(onCursorChanged);
        super.onDetachedFromWindow();
    }


    private BroadcastReceiver onCursorChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (observer != null) observer.onChanged();
        }

    };

    public void setAdapter(ServiceContext scontext, Iterator iterator) {
        setAdapter(new IteratorAdapter(scontext, iterator));
    };
    
    
    public class IteratorAdapter implements ListAdapter {
        private final Iterator iterator;
        private final ServiceContext scontext;

        public IteratorAdapter(ServiceContext sc, Iterator it) {
            iterator=it;
            scontext=sc;
        }


        @Override
        public int getCount() {
            return iterator.getCount();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            GpxListEntryView entry = (GpxListEntryView) convertView;

            if (entry == null) {
                entry = new GpxListEntryView(scontext, data);
            } 

            iterator.moveToPosition(position);
            entry.updateGpxContent(iterator.getInfo());

            return entry;
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
        public void registerDataSetObserver(DataSetObserver o) {
            observer=o;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver o) {
            observer=null;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }


    }

}
