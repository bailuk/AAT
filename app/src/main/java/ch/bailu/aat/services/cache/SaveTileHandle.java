package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import java.io.OutputStream;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public  class SaveTileHandle extends FileHandle {

    private final String sourceID;

    public SaveTileHandle(String source, Foc target) {
        super(target);
        sourceID = source;
    }


    @Override
    public long bgOnProcess(final ServiceContext sc) {
        final long[] size = {0};


        new OnObject(sc, sourceID, TileObject.class) {
            @Override
            public void run(ObjectHandle handle) {
                size[0] = save(sc, (TileObject) handle);
            }
        };

        return size[0];
    }


    private long save(ServiceContext sc, TileObject self) {
        long size = 0;
        OutputStream out = null;
        Foc file = getFile();

        if (file.exists() == false) {
            try {

                out = file.openW();

                Bitmap bitmap = self.getBitmap();
                if (bitmap != null && out != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
                }

                AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_ONDISK,
                        getFile().getPath(), sourceID);

                size = self.getSize();

            } catch (Exception e) {
                AppLog.d(this, e.toString());


            } finally {
                Foc.close(out);

            }
        }

        return size;
    }


}
