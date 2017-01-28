package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.map.tile.source.CachedSource;
import ch.bailu.aat.map.tile.source.Source;

public class SolidEnableTileCache extends SolidBoolean {
    private final CachedSource source;

    public SolidEnableTileCache(Context c, CachedSource s) {
        super(Storage.global(c),
                SolidEnableTileCache.class.getSimpleName() + s.getSource().getName());

        source = s;

    }



    @Override
    public String getLabel() {
        return "Enable cache*";
    }


    public Source getSourceCached() {
        return source;
    }

    public Source getSourceNotCached() {
        return source.getSource();
    }


    public Source getSource() {
        if (isEnabled()) return getSourceCached();
        return getSourceNotCached();
    }
}
