package ch.bailu.aat.map.osm;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class OsmViewStatic extends AbsOsmView {
    public OsmViewStatic(Context context, AbsTileProvider provider, MapDensity res) {
        super(context, provider, res);
    }

    @Override
    public String getSolidKey() {
        return OsmViewStatic.class.getSimpleName();
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
