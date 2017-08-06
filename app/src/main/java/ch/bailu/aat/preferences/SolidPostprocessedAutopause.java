package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public class SolidPostprocessedAutopause extends SolidAutopause {
    protected final static String KEY="pautopause";


    public SolidPostprocessedAutopause(Context c) {
        super(c, KEY, 0);
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_post_autopause);
    }

}
