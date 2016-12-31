package ch.bailu.aat.mapsforge.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;

import ch.bailu.aat.gpx.GpxInformation;

public abstract class MapsForgeLayer extends Layer implements MapsForgeLayerInterface {


    @Override
    public void onAttached() {}


    @Override
    public void onDetached() {}


    public static final MapsForgeLayer NULL = new MapsForgeLayer(){
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }

        @Override
        public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

        }


        @Override
        public void onLayout(boolean changed, int l, int t, int r, int b) {

        }
    };

}
