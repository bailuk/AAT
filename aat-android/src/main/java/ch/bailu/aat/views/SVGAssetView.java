package ch.bailu.aat.views;

import android.widget.ImageView;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.service.cache.icons.ObjSVGAsset;
import ch.bailu.aat_lib.service.icons.IconMapService;


public class SVGAssetView extends ImageObjectView {
    private final int size;

    public SVGAssetView(ServiceContext sc, int rid) {
        super(sc, rid);
        setScaleType(ImageView.ScaleType.CENTER);

         size = new AndroidAppDensity(sc.getContext()).toPixel_i(IconMapService.BIG_ICON_SIZE);
    }


    public void setImageObject(final int key, final String value) {
        scontext.insideContext(() -> setImageObject(scontext.getIconMapService().toAssetPath(key, value)));
    }

    public void setImageObject(final GpxPointNode gpxPointNode) {
        scontext.insideContext(() -> setImageObject(scontext.getIconMapService().toAssetPath(gpxPointNode)));
    }

    private void setImageObject(String name) {
        if (name != null) {

            String id = ObjSVGAsset.toID(name, size);
            setImageObject(id, new ObjSVGAsset.Factory(name, size));
        } else {
            setImageObject();
        }
    }
}
