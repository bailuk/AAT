package ch.bailu.aat.map.mapsforge.layer;

import android.content.SharedPreferences;

import ch.bailu.aat.map.mapsforge.util.Attachable;

public interface MapsForgeLayerInterface extends
        Attachable,
        SharedPreferences.OnSharedPreferenceChangeListener{

    void onLayout(boolean changed, int l, int t, int r, int b);
}