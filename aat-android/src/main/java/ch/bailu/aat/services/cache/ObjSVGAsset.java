package ch.bailu.aat.services.cache;

import com.caverock.androidsvg.SVG;

import org.mapsforge.core.graphics.Bitmap;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public final class ObjSVGAsset extends ObjImageAbstract {

    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final String name;
    private final int size;

    public ObjSVGAsset(String id, String name, int size) {
        super(id);
        this.name = name;
        this.size = size;
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
        return bitmap.getTileBitmap();
    }


    @Override
    public long getSize() {
        long size = bitmap.getSize();

        if (size == 0) size = Obj.MIN_SIZE;
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


    public static class Factory extends Obj.Factory {
        private final String name;
        private final int size;

        public Factory(String n, int s) {
            name = n;
            size = s;
        }


        @Override
        public Obj factory(String id, ServiceContext sc) {
            return new ObjSVGAsset(id, name, size);
        }
    }


    public static String toID(String name, int size) {
        if (name != null && name.length()>0)
            return ObjSVGAsset.class.getSimpleName() + "/" + name + "/" + size;

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

            new OnObject(sc, ID, ObjSVGAsset.class) {
                @Override
                public void run(Obj obj) {
                    ObjSVGAsset self = (ObjSVGAsset) obj;


                    try {
                        SVG svg = SVG.getFromAsset(sc.getContext().getAssets(), self.name);

                        self.bitmap.set(svg, self.size);
                        size[0] = self.size;


                    } catch (Exception e) {
                        self.setException(e);
                    }

                    OldAppBroadcaster.broadcast(sc.getContext(),
                           AppBroadcaster.FILE_CHANGED_INCACHE, ID);



                }
            };
            return size[0];
        }

    }
}
