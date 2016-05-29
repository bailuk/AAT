package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

public class GpxListSummaryView 
extends SummaryListView  {
    private static final String SOLID_KEY=GpxListSummaryView.class.getSimpleName();

    private final ServiceContext scontext;


    private BroadcastReceiver onCursorChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(scontext.getDirectoryService().getListSummary());
        }

    };

    public GpxListSummaryView(ServiceContext sc, ContentDescription[] data) {
        super(sc.getContext(), SOLID_KEY, GpxInformation.ID.INFO_ID_LIST_SUMMARY, data);

        scontext=sc;
        
        updateGpxContent(scontext.getDirectoryService().getListSummary());
        AppBroadcaster.register(getContext(), onCursorChanged, AppBroadcaster.DB_CURSOR_CHANGED);
    }





    @Override
    public void onDetachedFromWindow() {
        getContext().unregisterReceiver(onCursorChanged);
        super.onDetachedFromWindow();
    }

}
