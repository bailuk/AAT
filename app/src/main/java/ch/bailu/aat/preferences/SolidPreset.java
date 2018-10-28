package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;

public class SolidPreset extends SolidIndexList {

    final private static String KEY="preset";


    public SolidPreset(Context c) {
        super(c, KEY);
    }

    @Override
    public int length() {
        return new SolidPresetCount(getContext()).getValue();
    }

    @Override
    public String getValueAsString(int i) {
        return smet(i).getValueAsString();
    }

    @Override
    public String getValueAsString() {
        return smet().getValueAsString();
    }


    public SolidMET smet() {
        return smet(getIndex());
    }

    private SolidMET smet(int i) {
        return new SolidMET(getContext(), i);
    }


    @Override
    public boolean hasKey(String key) {
        return super.hasKey(key) || smet().hasKey(key);
    }

    

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_preset);
    }
    
    
    public Foc getDirectory() {
    	return AppDirectory.getTrackListDirectory(getContext(),getIndex());
    }
    
    
    public String getDirectoryName() {
    	return getDirectory().getPathName();
    }



}
