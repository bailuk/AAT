package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidDataDirectory extends SolidDirectoryList {
	private static final String KEY="TILE_DATA_DIR";
	private static final String[] POSTFIX={"aat_data"};
	
	public SolidDataDirectory(Context c) {
		super(c, KEY, POSTFIX);
	
	}
	
    @Override
    public String getLabel() {
        return "Data Directory*"; 
    }
}
