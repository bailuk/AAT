package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.JobQueue;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.MapWorkerPool;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

import java.util.ArrayList;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public class Renderer extends Layer {
    private final static boolean TRANSPARENT = false;
    private final static boolean RENDER_LABELS = true;
    private final static boolean CACHE_LABELS = false;
    private final static float TEXT_SCALE = 1f;

    private final MapDataStore mapDataStore;
    private final MapWorkerPool mapWorkerPool;
    private final RenderThemeFuture renderThemeFuture;


    private final JobQueue<RendererJob> jobQueue;



    public Renderer(XmlRenderTheme t, TileCache cache, ArrayList<Foc> files) {
        final Model model = new Model();


        displayModel=model.displayModel;
        jobQueue = new JobQueue<>(model.mapViewPosition, model.displayModel);

        renderThemeFuture = createTheme(t);

        if (files.size() == 1) {
            mapDataStore = new MapFile(files.get(0).toString());

        } else {
            MultiMapDataStore store = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);

            for (Foc f : files) {
                try {
                    // TODO: Translate FocContent to unix file

                    store.addMapDataStore(new MapFile(f.toString()), true, true);
                    AppLog.d(this, "Add \'" + f.getPathName() + "\' to renderer");
                } catch (Exception e) {
                    AppLog.d(this, e.toString());
                }
            }

            mapDataStore = store;
        }


        final  DatabaseRenderer databaseRenderer = new DatabaseRenderer(
                mapDataStore,
                AndroidGraphicFactory.INSTANCE,
                cache,
                null,
                RENDER_LABELS,
                CACHE_LABELS, null);



        mapWorkerPool = new MapWorkerPool(
                cache,
                jobQueue,
                databaseRenderer,
                this);



        mapWorkerPool.start();
    }


    private static RenderThemeFuture createTheme(XmlRenderTheme t) {
        RenderThemeFuture theme = new RenderThemeFuture(
                AndroidGraphicFactory.INSTANCE,
                t,
                new Model().displayModel);
        new Thread(theme).start();
        return theme;
    }

    public void destroy() {
        mapWorkerPool.stop();
        mapDataStore.close();
        renderThemeFuture.decrementRefCount();
    }


    public void addJob(Tile tile) {
        if (mapDataStore.supportsTile(tile)) {
            jobQueue.add(createJob(tile));
        }
    }


    private RendererJob createJob(Tile tile) {
        return new RendererJob(tile, mapDataStore,
                renderThemeFuture,
                displayModel,
                TEXT_SCALE,
                TRANSPARENT, false);
    }


    @Override
    public void draw(BoundingBox b, byte z, Canvas c, Point t) {}

    public boolean supportsTile(Tile t) {
        return mapDataStore.supportsTile(t);
    }

}
