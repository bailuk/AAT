package ch.bailu.aat.mapsforge.layer;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.mapsforge.Attachable;

public interface MapsForgeLayerInterface extends Attachable, OnContentUpdatedInterface {

    void onSharedPreferenceChanged(String key);
    void onLayout(boolean changed, int l, int t, int r, int b);
}