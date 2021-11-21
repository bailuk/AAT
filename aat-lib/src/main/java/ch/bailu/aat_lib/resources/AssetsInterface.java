package ch.bailu.aat_lib.resources;

import ch.bailu.aat_lib.factory.FocFactory;

public interface AssetsInterface extends FocFactory {

    /**
     *  context.getAssets()
     *  AssetManager assets
     *  new FocAsset(assets, MAP_FEATURES_ASSET + "/" + f);
     *
     * @param mapFeaturesAsset
     * @return
     */
    String[] list(String mapFeaturesAsset);
}
