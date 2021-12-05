package ch.bailu.aat_lib.resources;

import ch.bailu.aat_lib.factory.FocFactory;

public interface AssetsInterface extends FocFactory {

    String[] list(String assetsPath);
}
