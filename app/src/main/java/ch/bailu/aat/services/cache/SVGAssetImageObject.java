package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import com.caverock.androidsvg.SVG;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat.util.ui.AppLog;

public final class SVGAssetImageObject extends ImageObjectAbstract {

    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final String name;
    private final int size;

    public SVGAssetImageObject(String id, String name, int size) {
        super(id);
        this.name = name;
        this.size = size;

        AppLog.d(this, name + " s: " + size);

    }


    @Override
    public void onInsert(ServiceContext sc){
        load(sc);
    }

    private void load(ServiceContext sc) {
        sc.getBackgroundService().process(new SvgLoader(getID()));
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap.getAndroidBitmap();
    }


    @Override
    public long getSize() {
        long size = bitmap.getSize();

        if (size == 0) size = ObjectHandle.MIN_SIZE;
        return size;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {

    }

    @Override
    public void onChanged(String id, ServiceContext sc) {

    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }


    public static class Factory extends ObjectHandle.Factory {
        private final String name;
        private final int size;

        public Factory(String n, int s) {
            name = n;
            size = s;
        }


        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return new SVGAssetImageObject(id, name, size);
        }
    }


    public static String toID(String name, int size) {
        if (name != null && name.length()>0)
            return SVGAssetImageObject.class.getSimpleName() + "/" + name + "/" + size;

        return null;
    }


    private static class SvgLoader extends BackgroundTask  {

        private final String ID;
        public SvgLoader(String id) {
            ID = id;

        }

        @Override
        public long bgOnProcess(final ServiceContext sc) {
            final long[] size = {0};

            new OnObject(sc, ID, SVGAssetImageObject.class) {
                @Override
                public void run(ObjectHandle obj) {
                    SVGAssetImageObject self = (SVGAssetImageObject) obj;


                    try {
                        SVG svg = SVG.getFromAsset(sc.getContext().getAssets(), self.name);

                        self.bitmap.set(svg, self.size);
                        size[0] = self.size;


                    } catch (Exception e) {
                        self.setException(e);
                    }

                    AppBroadcaster.broadcast(sc.getContext(),
                           AppBroadcaster.FILE_CHANGED_INCACHE, ID);



                }
            };
            return size[0];
        }

    }
}
