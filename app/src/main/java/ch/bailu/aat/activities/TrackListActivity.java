package ch.bailu.aat.activities;

import android.content.Intent;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.preferences.presets.SolidPreset;
import ch.bailu.util_java.foc.Foc;

public class TrackListActivity extends AbsGpxListActivity {

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
    public ContentDescription[] getSummaryData() {
        return new ContentDescription[] {

                new TrackSizeDescription(this),
                new MaximumSpeedDescription(this),
                new DistanceDescription(this),
                new AverageSpeedDescription(this),
                new TimeDescription(this)
        };
    }

    @Override
    public void displayFile() {
        Intent intent=new Intent(this,FileContentActivity.class);
        startActivity(intent);
    }


    @Override
    public Foc getDirectory() {
        return new SolidPreset(this).getDirectory();
    }

    @Override
    public String getLabel() {
        return getString(R.string.intro_list);
    }

}
