package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.services.directory.DirectoryService;

public class GpxListSummaryView 
extends SummaryListView  implements CleanUp {
    private static final String SOLID_KEY=GpxListSummaryView.class.getSimpleName();

    private final DirectoryService directory;


    private BroadcastReceiver onCursorChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(directory.getListSummary());
        }

    };

    public GpxListSummaryView(Context context, DirectoryService d,
            ContentDescription[] data) {
        super(context, SOLID_KEY, GpxInformation.ID.INFO_ID_LIST_SUMMARY, data);


        directory = d;
        updateGpxContent(directory.getListSummary());
        AppBroadcaster.register(directory, onCursorChanged, AppBroadcaster.DB_CURSOR_CHANGED);
    }





    @Override
    public void cleanUp() {
        directory.unregisterReceiver(onCursorChanged);
        super.cleanUp();
    }

}
