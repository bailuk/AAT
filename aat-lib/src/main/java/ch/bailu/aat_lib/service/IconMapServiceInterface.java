package ch.bailu.aat_lib.service;

import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat_lib.service.cache.ObjeImageInterface;

public interface IconMapServiceInterface {
    ObjeImageInterface getIconSVG(GpxPointInterface point, int icon_size);

    String toAssetPath(int key, String value);

    String toAssetPath(GpxPointNode gpxPointNode);
}
