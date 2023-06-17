package ch.bailu.aat.views;

import android.content.Intent;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.app.ActivitySwitcher;
import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.util.ui.theme.AppTheme;
import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxInformationCache;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListArray;

public class NodeListView extends ListView implements
        OnContentUpdatedInterface,
        ListAdapter,
        android.widget.AdapterView.OnItemClickListener
{
    private final static UiTheme THEME = AppTheme.search;

    private final SparseArray<DataSetObserver> observer= new SparseArray<>(5);

    private GpxListArray array = new GpxListArray(GpxList.NULL_ROUTE);

    private final GpxInformationCache cachedInfo = new GpxInformationCache();

    private final ActivityContext dispatcher;

    public NodeListView(ActivityContext activityContext) {
        super(activityContext);
        dispatcher = activityContext;

        THEME.list(this);

        setAdapter(this);
        setOnItemClickListener(this);

    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        array = new GpxListArray(info.getGpxList());
        cachedInfo.set(iid, info);
        notifyDataSetChanged();

    }


    private void notifyDataSetChanged() {
        for (int i=0; i<observer.size(); i++) {
            observer.valueAt(i).onChanged();
        }
    }

    @Override
    public int getCount() {
        return array.size();
    }


    @Override
    public View getView(int position, View recycle, ViewGroup parent) {

        NodeEntryView entry = (NodeEntryView) recycle;

        if (entry == null) {
            entry = new NodeEntryView(dispatcher);
        }

        entry.update(cachedInfo.infoID, cachedInfo.info, array.get(position));
        return entry;
    }




    @Override
    public Object getItem(int position) {
        return array.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        final Intent intent = new Intent();
        intent.putExtra("I", pos);
        intent.putExtra("ID", cachedInfo.info.getFile().toString());
        ActivitySwitcher.start(getContext(), NodeDetailActivity.class, intent);

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
    public void registerDataSetObserver(DataSetObserver o) {
        observer.put(o.hashCode(), o);
        notifyDataSetChanged();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver o) {
        observer.delete(o.hashCode());
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
