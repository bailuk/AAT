package ch.bailu.aat.services.directory;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.activities.AbsServiceLink;
import ch.bailu.aat.preferences.SolidFilterFrom;
import ch.bailu.aat.preferences.SolidFilterTo;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidTrackListFilter;
import ch.bailu.aat.preferences.Storage;

public class DynamicDirectoryServiceHelper extends DirectoryServiceHelper 
    implements OnSharedPreferenceChangeListener {


    private final SolidTrackListFilter sfilter;
    private final SolidFilterFrom sfrom;
    private final SolidFilterTo sto;
    private final Storage storage;


    public DynamicDirectoryServiceHelper(AbsServiceLink l)  {
        super(l,getDirectory(l));

        int preset = new SolidPreset(l).getIndex();

        sfilter = new SolidTrackListFilter(l, preset);
        sfrom = new SolidFilterFrom(l, preset);;
        sto = new SolidFilterTo(l, preset);;

        storage = Storage.preset(l);
        storage.register(this);
        
        setSelectionString(createSelectionString());
    }

    
    private static File getDirectory(Context c) {
        return new SolidPreset(c).getDirectory();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences p, String key) {

        if (sfilter.hasKey(key) || sfrom.hasKey(key) || sto.hasKey(key)) {
            requery(createSelectionString());
        }
    }

    private String createSelectionString() {
        String selection="";


        if (sfilter.getIndex()==1) {
            selection = GpxDbConstants.KEY_START_TIME 
                    + " BETWEEN " 
                    + String.valueOf(Math.min(sfrom.getValue(), sto.getValue())) 
                    + " AND " 
                    + String.valueOf(Math.max(sfrom.getValue(), sto.getValue())) ;
        }
        return selection;
    }



    @Override
    public void close() {
        storage.unregister(this);
        super.close();
    }
}
