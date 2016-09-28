package ch.bailu.aat.preferences;

import java.io.File;

import android.content.Context;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppDirectory;

public class SolidPreset extends SolidStaticIndexList {
    final private static String KEY="preset";
    final private static String[] NAMES={"A0", "A1", "A2", "A3", "A4"};
   
    
    public SolidPreset(Context c) {
        super(Storage.global(c), KEY, NAMES);
    }

    
    public String getValueAsString() {
        SolidMET smet = new SolidMET(getContext(), getIndex());
        return smet.getValueAsString();
    }
    
    @Override
    public String[] getStringArray() {
        
        String[] array = new String[length()];
        
        for (int i=0; i< array.length; i++) {
            SolidMET smet = new SolidMET(getContext(), i);
            array[i] = smet.getValueAsString();
        }
        return array;
    }
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_preset);
    }
    
    
    public File getDirectory() {
    	return AppDirectory.getTrackListDirectory(getContext(),getIndex());
    }
    
    
    public String getDirectoryName() {
    	return getDirectory().toString();
    }
    
    public String getCacheDbName() {
    	return AppDirectory.getTrackListCacheDb(getContext(),getIndex()).toString();
    }
    
    
}
