package ch.bailu.aat.views;

import android.widget.ImageView;

import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.cache.icons.ObjSVGAsset;
import ch.bailu.aat_lib.service.icons.IconMapService;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat_lib.gpx.GpxPointNode;


public class SVGAssetView extends ImageObjectView {
    private final int size;

    public SVGAssetView(ServiceContext sc, int rid) {
        super(sc, rid);
        setScaleType(ImageView.ScaleType.CENTER);

         size = new AndroidAppDensity(sc).toPixel_i(IconMapService.BIG_ICON_SIZE);
    }


    public void setImageObject(final int key, final String value) {

        new InsideContext(scontext) {
            @Override
            public void run() {
                setImageObject(scontext.getIconMapService().toAssetPath(key, value));
            }
        };


    }

    public void setImageObject(final GpxPointNode gpxPointNode) {


        new InsideContext(scontext) {
            @Override
            public void run() {
                setImageObject(scontext.getIconMapService().toAssetPath(gpxPointNode));

            }
        };
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
