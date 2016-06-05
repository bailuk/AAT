package ch.bailu.aat.services.directory;

import java.io.File;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.preferences.SolidFilterFrom;
import ch.bailu.aat.preferences.SolidFilterTo;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidTrackListFilter;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class DynamicDirectoryServiceHelper extends DirectoryServiceHelper 
    implements OnSharedPreferenceChangeListener {


    private final SolidTrackListFilter sfilter;
    private final SolidFilterFrom sfrom;
    private final SolidFilterTo sto;
    private final Storage storage;


    public DynamicDirectoryServiceHelper(ServiceContext sc)  {
        super(sc,getDirectory(sc));

        int preset = new SolidPreset(sc.getContext()).getIndex();

        sfilter = new SolidTrackListFilter(sc.getContext(), preset);
        sfrom = new SolidFilterFrom(sc.getContext(), preset);;
        sto = new SolidFilterTo(sc.getContext(), preset);;

        storage = Storage.preset(sc.getContext());
        storage.register(this);
        
        setSelectionString(createSelectionString());
    }

    
    private static File getDirectory(ServiceContext sc) {
        return new SolidPreset(sc.getContext()).getDirectory();
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
