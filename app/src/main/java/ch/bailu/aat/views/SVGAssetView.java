package ch.bailu.aat.views;

import android.widget.ImageView;

import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.SVGAssetImageObject;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.util.ui.AppDensity;


public class SVGAssetView extends ImageObjectView {
    private final int size;

    public SVGAssetView(ServiceContext sc, int rid) {
        super(sc, rid);
        setScaleType(ImageView.ScaleType.CENTER);

         size = new AppDensity(sc).toPixel_i(IconMapService.BIG_ICON_SIZE);
    }


    public void setImageObject(final String key, final String value) {

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

            String id = SVGAssetImageObject.toID(name, size);
            setImageObject(id, new SVGAssetImageObject.Factory(name, size));
        } else {
            setImageObject();
        }
    }
}
