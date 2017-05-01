package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.MapWorkerPool;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.util.ui.AppLog;

public class Renderer extends RendererBase<RendererJob> {
    private final static boolean TRANSPARENT = false;
    private final static boolean RENDER_LABELS = true;
    private final static boolean CACHE_LABELS = false;
    private final static float TEXT_SCALE = 1f;

    private final MapDataStore mapDataStore;
    private final MapWorkerPool mapWorkerPool;
    private final RenderThemeFuture renderThemeFuture;

    public Renderer(RenderThemeFuture rt, TileCache cache, ArrayList<File> files) {
        super(cache, new Model()); // TODO: move model to context

        renderThemeFuture = rt;

        if (files.size()==1) {
            mapDataStore = new MapFile(files.get(0));

        } else {
            MultiMapDataStore store= new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);

            for (File f: files) {
                AppLog.d(this, f.toString() + ": add_w to renderer");
                store.addMapDataStore(new MapFile(f), true, true);
            }

            mapDataStore = store;
        }


        final  DatabaseRenderer databaseRenderer = new DatabaseRenderer(
                mapDataStore,
                AndroidGraphicFactory.INSTANCE, // TODO: move to context
                cache,
                null,
                RENDER_LABELS,
                CACHE_LABELS);



        mapWorkerPool = new MapWorkerPool(
                cache,
                jobQueue,
                databaseRenderer,
                this);


        mapWorkerPool.start();

    }



    public void destroy() {
        mapWorkerPool.stop();
        mapDataStore.close();
    }




    @Override
    protected RendererJob createJob(Tile tile) {
        return new RendererJob(tile, mapDataStore,
                renderThemeFuture,
                displayModel,
                TEXT_SCALE,
                TRANSPARENT, false);
    }

    @Override
    protected boolean isTileStale(Tile tile, TileBitmap bitmap) {
        return mapDataStore.getDataTimestamp(tile) > bitmap.getTimestamp();
    }


    @Override
    public void draw(BoundingBox b, byte z, Canvas c, Point t) {}
}
