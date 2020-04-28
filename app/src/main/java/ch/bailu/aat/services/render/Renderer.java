package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.Canvas;
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
import org.mapsforge.map.reader.header.MapFileException;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

import java.util.ArrayList;

import ch.bailu.util_java.foc.Foc;

public final class Renderer extends Layer {
    private final static boolean TRANSPARENT = false;
    private final static boolean RENDER_LABELS = true;
    private final static boolean CACHE_LABELS = false;
    private final static float TEXT_SCALE = 1f;

    private final MapDataStore mapDataStore;
    private final MapWorkerPool mapWorkerPool;
    private final RenderThemeFuture renderThemeFuture;


    private final JobQueue<RendererJob> jobQueue;



    public Renderer(XmlRenderTheme t, TileCache cache, ArrayList<Foc> files) throws Exception {
        try {
            final Model model = new Model();

            displayModel = model.displayModel;
            jobQueue = new JobQueue<>(model.mapViewPosition, model.displayModel);

            renderThemeFuture = createTheme(t);
            mapDataStore = createMapDataStore(files);

            final DatabaseRenderer databaseRenderer = new DatabaseRenderer(
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
        } catch (Exception e) {
            destroy();
            throw e;
        }
    }




    private MapDataStore createMapDataStore(ArrayList<Foc> files) throws Exception {
        MapDataStore result;
        ArrayList<MapFile> mapFiles = createMapFiles(files);

        if (mapFiles.size()==1) {
            result = mapFiles.get(0);
        } else {
            result = createMultiMapDataStore(mapFiles);
        }

        return result;
    }


    private MapDataStore createMultiMapDataStore(ArrayList<MapFile> mapFiles) {
        MultiMapDataStore result = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
        for (MapFile mapFile : mapFiles) {
            result.addMapDataStore(mapFile, true, true);
        }
        return result;
    }


    private ArrayList<MapFile> createMapFiles(ArrayList<Foc> files) throws Exception {
        Exception exception = new MapFileException("No file specified");

        ArrayList<MapFile> result = new ArrayList<>(files.size());

        for (Foc file : files) {
            try {
                result.add(new MapFile(file.toString()));
            } catch (Exception e) {
                exception = e;
            }
        }
        if (result.size() == 0) {
            throw exception;
        }

        return result;
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
        if (mapWorkerPool != null) {
            mapWorkerPool.stop();
        }

        if (mapDataStore != null) {
            mapDataStore.close();
        }

        if (renderThemeFuture != null) {
            renderThemeFuture.decrementRefCount();
        }
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
