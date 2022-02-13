package ch.bailu.aat_lib.service.cache;

import org.mapsforge.core.graphics.TileBitmap;

import java.io.OutputStream;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.background.FileTask;
import ch.bailu.foc.Foc;

public final class SaveTileTask extends FileTask {

    private final String sourceID;

    public SaveTileTask(String source, Foc target) {
        super(target);
        sourceID = source;
    }


    @Override
    public long bgOnProcess(final AppContext sc) {
        final long[] size = {0};


        new OnObject(sc, sourceID, ObjTile.class) {
            @Override
            public void run(Obj handle) {
                size[0] = save(sc, (ObjTile) handle);
            }
        };

        return size[0];
    }


    private long save(AppContext sc, ObjTile self) {
        long size = 0;
        OutputStream out = null;
        Foc file = getFile();

        if (!file.exists()) {
            try {

                out = file.openW();

                TileBitmap bitmap = self.getTileBitmap();
                if (bitmap != null && out != null) {
                    bitmap.compress(out);
                }

                sc.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_ONDISK,
                        getFile().toString(), sourceID);

                size = self.getSize();

            } catch (Exception e) {
                AppLog.w(this, e);


            } finally {
                Foc.close(out);

            }
        }

        return size;
    }
}
