package ch.bailu.aat.preferences;

import ch.bailu.aat.R;
import android.content.Context;

public class SolidTileCacheDirectory extends SolidDirectoryList {
	private static final String KEY="TILE_CACHE_DIR";
	private static final String[] POSTFIX={"osmdroid/tiles", "aat_data/tiles"};
	
	public SolidTileCacheDirectory(Context c) {
		super(c, KEY, POSTFIX);
	
	}

	
	@Override
	    public String getLabel() {
	        return getContext().getString(R.string.p_directory_tiles); 
	    }
}
