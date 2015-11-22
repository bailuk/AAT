package ch.bailu.aat.preferences;

import ch.bailu.aat.R;
import android.content.Context;

public class SolidTrackListFilter extends SolidStaticIndexList {
    private final static String KEY="filter";
    
    
    public SolidTrackListFilter(Context c, int i) {
        super(
                Storage.preset(c), 
                KEY+i, 
                new String[] {
                    c.getResources().getString(R.string.on), 
                    c.getResources().getString(R.string.off)
                }
        );
        
    }

    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.filter);
    }
}
