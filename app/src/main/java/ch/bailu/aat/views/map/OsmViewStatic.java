package ch.bailu.aat.views.map;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;

import ch.bailu.aat.gpx.GpxInformation;

public class OsmViewStatic extends AbsOsmView {



    public OsmViewStatic(Context context, AbsTileProvider provider) {
        super(context, provider);
    }




    @Override
    public void onContentUpdated(GpxInformation info) {
        if (filter.pass(info)) {
            super.onContentUpdated(info);
            map.invalidate();
        }
    }


 



    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        return false;

    }

    @Override
    public boolean onTrackballEvent(final MotionEvent event) {

        return false;
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent event) {
        return false;
    }


}
