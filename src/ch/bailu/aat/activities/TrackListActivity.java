package ch.bailu.aat.activities;

import java.io.IOException;

import android.content.Intent;
import android.widget.LinearLayout;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.directory.DirectoryServiceHelper;
import ch.bailu.aat.services.directory.DynamicDirectoryServiceHelper;
import ch.bailu.aat.views.DateFilterView;
import ch.bailu.aat.views.GpxListSummaryView;

public class TrackListActivity extends AbsGpxListActivity {

    private GpxListSummaryView summaryView;


    @Override
    public LinearLayout createHeader(LinearLayout contentView) {
        return createDateFilter(contentView);
    }

    private LinearLayout createDateFilter(LinearLayout contentView) {
        LinearLayout dateFilter = new DateFilterView(this);
        contentView.addView(dateFilter);

        return dateFilter;
    }


    public void createSummaryView(LinearLayout layout, DirectoryService directory) {
        ContentDescription data[] = new ContentDescription[] {
                new MaximumSpeedDescription(this),
                new DistanceDescription(this),
                new AverageSpeedDescription(this),
                new TimeDescription(this)
        };   

        summaryView = new GpxListSummaryView(this, directory,  data);
        
        layout.addView(summaryView, AppLayout.getScreenSmallSide(this), AppLayout.getScreenSmallSide(this) /3);
    }


    @Override
    public void onDestroy() {
        if (summaryView != null) summaryView.cleanUp();
        summaryView = null;
        super.onDestroy();
    }

    @Override
    public DirectoryServiceHelper createDirectoryServiceHelper(
            DirectoryService directory) throws IOException {
        return new DynamicDirectoryServiceHelper(directory);
    }

    @Override
    public ContentDescription[] getGpxListItemData() {
        return new ContentDescription[] {
                new DateDescription(this),
                new DistanceDescription(this),
                new AverageSpeedDescription(this),
                new TimeDescription(this),
                new NameDescription(this)
        };    		
    }

    @Override
    public void displayFile() {
        Intent intent=new Intent(this,FileContentActivity.class);
        startActivity(intent);
    }
    
   

}
