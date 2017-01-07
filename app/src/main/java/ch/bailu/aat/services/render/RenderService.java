package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.cache.MapsForgeTileObject;

public class RenderService  extends VirtualService {
    private final Cache cache = new Cache();
    private final RendererList rendererList = new RendererList(cache);

    private final MapList mapList = new MapList(new File("/storage/C973-F26F/mapsforge"));



    public RenderService(ServiceContext sc) {
        super(sc);
    }



    public TileBitmap getTile(Tile tile) {
        ArrayList<File> files = mapList.getFiles(tile);
        return rendererList.getTile(files, tile);
    }


    public void lockToCache(MapsForgeTileObject o) {
        cache.lockToCache(o);
    }
    public void freeFromCache(MapsForgeTileObject o) {
        cache.freeFromCache(o);
    }



    @Override
    public void appendStatusText(StringBuilder builder) {

    }

    @Override
    public void close() {
        rendererList.destroy();
        cache.destroy();
    }
}
