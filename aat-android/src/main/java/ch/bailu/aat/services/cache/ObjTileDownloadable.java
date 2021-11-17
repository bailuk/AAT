package ch.bailu.aat.services.cache;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.background.DownloadTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.foc.Foc;

public class ObjTileDownloadable extends ObjTileCacheOnly {

    private final DownloadSource source;


    public ObjTileDownloadable(String id, ServiceContext sc, Tile t, DownloadSource s) {
        super(id, sc, t, s);
        source=s;
    }


    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable()) {
            load(sc);
        } else if (isDownloadable() && !isSheduled(sc) && !fileExists()) {
            download(sc);
        }
    }


    private void download(ServiceContext sc) {
        final String url = source.getTileURLString(getTile());
        sc.getBackgroundService().process(new FileDownloader(url, getFile(), sc));
    }


    private boolean isSheduled(ServiceContext sc) {
        return sc.getBackgroundService().findTask(getFile()) != null;
    }


    @Override
    public void reDownload(ServiceContext sc) {
        if (isDownloadable() && !isSheduled(sc)) {
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

        private final ServiceContext scontext;

        public FileDownloader(String source, Foc target, ServiceContext sc)  {
            super(sc.getContext(), source, target);
            scontext = sc;
        }


        @Override
        public long bgOnProcess(ServiceContext sc) {
            if (isInCache()) {
                return super.bgOnProcess(sc);
            }
            return 0;
        }

        private boolean isInCache() {
            final boolean[] result = {false};

            new OnObject(scontext, getFile().getPath(), ObjTileCacheOnly.class) {
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
        public Obj factory(String id, ServiceContext cs) {
            return new ObjTileDownloadable(id, cs, tile, source);
        }
    }
}
