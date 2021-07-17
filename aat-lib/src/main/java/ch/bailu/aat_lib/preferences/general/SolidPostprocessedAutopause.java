package ch.bailu.aat_lib.preferences.general;

import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidPostprocessedAutopause extends SolidAutopause {
    protected final static String KEY="pautopause";


    public SolidPostprocessedAutopause(StorageInterface s, int preset) {
        super(s, KEY, preset);
    }


    @Override
    public String getLabel() {
        return Res.str().p_post_autopause();
    }
}
