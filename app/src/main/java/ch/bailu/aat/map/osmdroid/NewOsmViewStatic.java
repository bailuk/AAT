package ch.bailu.aat.map.osmdroid;

import android.view.KeyEvent;
import android.view.MotionEvent;

import ch.bailu.aat.services.ServiceContext;

public class NewOsmViewStatic extends NewOsmView {
    public NewOsmViewStatic(ServiceContext sc, AbsTileProvider provider, MapDensity r) {
        super(sc, provider, r, NewOsmViewStatic.class.getSimpleName());
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
