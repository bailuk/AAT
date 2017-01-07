package ch.bailu.aat.services.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.graphic.AppBitmap;
import ch.bailu.aat.util.graphic.SynchronizedBitmap;
import ch.bailu.aat.util.ui.AppLog;

public class SVGAssetImageObject extends ImageObjectAbstract {

    private final SynchronizedBitmap bitmap = new SynchronizedBitmap();


    public SVGAssetImageObject(ServiceContext sc, String id, String name, int size) {
        super(id);

        try {
            SVG svg = SVG.getFromAsset(sc.getContext().getAssets(), name);

            AppBitmap b = new AppBitmap(size, size, true);
            Picture p = svg.renderToPicture();
            Canvas c = b.getAndroidCanvas();
            c.drawPicture(p, new RectF(0,0,size, size));

            bitmap.set(b);

        } catch (SVGParseException e) {
            AppLog.e(sc.getContext(), e);
        } catch (IOException e) {
            AppLog.e(sc.getContext(), e);
        }
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap.getAndroidBitmap();
    }

    @Override
    public Drawable getDrawable(Resources res) {
        return bitmap.getDrawable(res);
    }

    @Override
    public AppBitmap getAppBitmap() {
        return bitmap.get();
    }

    @Override
    public long getSize() {
        return bitmap.getSize();
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
            return new SVGAssetImageObject(sc, id, name, size);
        }
    }


    public static String toID(String name, int size) {
        return SVGAssetImageObject.class.getSimpleName() + "/" + name + "/" + size;

    }

}
