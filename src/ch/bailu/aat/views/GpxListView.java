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
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;

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


    public void setAdapter(final ServiceContext scontext) {
        setAdapter(new ListAdapter() {
            @Override
            public int getCount() {
                return scontext.getDirectoryService().size();
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

                GpxInformation info;
                scontext.getDirectoryService().setPosition(position);
                info = scontext.getDirectoryService().getCurrent();


                entry.updateGpxContent(info);

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
        });

    };
}
