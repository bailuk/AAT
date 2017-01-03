package ch.bailu.aat.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.directory.Iterator;
import ch.bailu.aat.services.directory.Iterator.OnCursorChangedListener;

public class GpxListView extends ListView implements OnCursorChangedListener {


    private DataSetObserver observer;

    private final ContentDescription data[];


    public GpxListView(Context c, 
            ContentDescription cd[]) {
        super(c);

        data = cd;


        AppTheme.themify(this, AppTheme.getHighlightColor());
    }


    public void setIterator(ServiceContext scontext, Iterator iterator) {
        iterator.setOnCursorChangedLinsener(this);
        setAdapter(new IteratorAdapter(scontext, iterator));
    }
    

    @Override
    public void onCursorChanged() {
        if (observer != null) observer.onChanged();
    }

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
            entry.onContentUpdated(iterator.getInfoID(), iterator.getInfo());


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
