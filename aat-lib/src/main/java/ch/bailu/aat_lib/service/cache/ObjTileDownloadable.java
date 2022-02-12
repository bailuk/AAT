package ch.bailu.aat_lib.service.cache;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.DownloadTask;
import ch.bailu.foc.Foc;

public class ObjTileDownloadable extends ObjTileCacheOnly {

    private final DownloadSource source;


    public ObjTileDownloadable(String id, AppContext sc, Tile t, DownloadSource s) {
        super(id, sc, t, s);
        source=s;
    }


    @Override
    public void onInsert(AppContext sc) {
        if (isLoadable()) {
            load(sc.getServices());
        } else if (isDownloadable() && !isScheduled(sc.getServices()) && !fileExists()) {
            download(sc);
        }
    }


    private void download(AppContext sc) {
        final String url = source.getTileURLString(getTile());
        sc.getServices().getBackgroundService().process(new FileDownloader(url, getFile(), sc));
    }


    private boolean isScheduled(ServicesInterface sc) {
        return sc.getBackgroundService().findTask(getFile()) != null;
    }


    @Override
    public void reDownload(AppContext sc) {
        if (isDownloadable() && !isScheduled(sc.getServices())) {
            getFile().rm();
            download(sc);
        }
    }


    private boolean isDownloadable() {
        return (
                source.getMaximumZoomLevel() >= getTile().zoomLevel &&
                        source.getMinimumZoomLevel() <= getTile().zoomLevel
        );
    }


    private static class FileDownloader extends DownloadTask {

        final AppContext appContext;
        public FileDownloader(String source, Foc target, AppContext sc)  {
            super(source, target,sc.getDownloadConfig());
            this.appContext = sc;
        }


        @Override
        public long bgOnProcess(AppContext sc) {
            if (isInCache()) {
                return super.bgOnProcess(sc);
            }
            return 0;
        }

        private boolean isInCache() {
            final boolean[] result = {false};

            new OnObject(appContext, getFile().toString(), ObjTileCacheOnly.class) {
                @Override
                public void run(Obj handle) {
                    result[0] = true;
                }
            };
            return result[0];
        }
    }


    public static class Factory extends Obj.Factory {
        private final Tile tile;
        private final DownloadSource source;


        public Factory(Tile t, DownloadSource s) {
            tile = t;
            source = s;
        }

        @Override
        public Obj factory(String id, AppContext appContext) {
            return new ObjTileDownloadable(id, appContext, tile, source);
        }
    }
}
