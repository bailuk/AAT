package ch.bailu.aat.services.render;

import android.util.SparseArray;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.labels.TileBasedLabelStore;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.layer.queue.JobQueue;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.MapWorkerPool;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

import java.io.File;
import java.util.Set;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.cache.MapsForgeTileObject;
import ch.bailu.aat.util.ui.AppLog;

public class RenderService  extends VirtualService {
    private final static boolean TRANSPARENT = false;
    private final static boolean RENDER_LABELS = true;
    private final static boolean CACHE_LABELS = false;
    private final static boolean HAS_JOB_QUEUE = true;

    private final GraphicFactory graphicFactory;
    private final Model model = new Model();
    private final Cache tileCache = new Cache();
    private final MapDataStore mapDataStore =
            new MapFile(new File("/storage/emulated/0/switzerland.map"));

    private final RenderLayer renderer;

    public RenderService(ServiceContext sc) {
        super(sc);

        graphicFactory = AndroidGraphicFactory.INSTANCE;

        renderer = new RenderLayer();
        renderer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
    }



    public TileBitmap getTile(Tile tile) {
        return renderer.getTile(tile);
    }


    public void lockToCache(MapsForgeTileObject o) {
        tileCache.lockToCache(o);
    }
    public void freeFromCache(MapsForgeTileObject o) {
        tileCache.freeFromCache(o);
    }


    private abstract class BaseLayer<T extends Job> extends Layer{
        protected final JobQueue<T> jobQueue;

        public BaseLayer() {
            displayModel=model.displayModel;
            jobQueue = new JobQueue<>(model.mapViewPosition, model.displayModel);

        }

        public TileBitmap getTile(Tile tile) {
            T job = createJob(tile);
            TileBitmap bitmap = tileCache.getImmediately(job);

            if (bitmap == null) {
                if (HAS_JOB_QUEUE && !tileCache.containsKey(job)) {
                    jobQueue.add(job);
                }

            } else {
                if (isTileStale(tile, bitmap) && HAS_JOB_QUEUE && !tileCache.containsKey(job)) {
                    jobQueue.add(job);
                }
                retrieveLabelsOnly(job);
            }

            if (HAS_JOB_QUEUE) {
                jobQueue.notifyWorkers();
            }
            return bitmap;
        }


        protected abstract T createJob(Tile tile);


        protected abstract boolean isTileStale(Tile tile, TileBitmap bitmap);

        protected void retrieveLabelsOnly(T job) {
        }

    }

    private class RenderLayer extends BaseLayer<RendererJob> {
        private final DatabaseRenderer databaseRenderer;
        private final MapWorkerPool mapWorkerPool;
        private RenderThemeFuture renderThemeFuture;
        private final static float TEXT_SCALE = 1f;
        private XmlRenderTheme xmlRenderTheme;

        public RenderLayer() {
            databaseRenderer = new DatabaseRenderer(
                    mapDataStore,
                    graphicFactory,
                    tileCache,
                    null,
                    RENDER_LABELS,
                    CACHE_LABELS);




            compileRenderTheme();

            mapWorkerPool = new MapWorkerPool(
                    tileCache,
                    jobQueue,
                    databaseRenderer,
                    this);


            mapWorkerPool.start();

        }


          public void destroy() {
            mapWorkerPool.stop();
            if (renderThemeFuture != null) {
                renderThemeFuture.decrementRefCount();
            }
            mapDataStore.close();
        }

          public void setXmlRenderTheme(XmlRenderTheme xmlRenderTheme) {
            this.xmlRenderTheme = xmlRenderTheme;
            compileRenderTheme();
        }

        protected void compileRenderTheme() {
            this.renderThemeFuture = new RenderThemeFuture(
                    graphicFactory,
                    xmlRenderTheme,
                    model.displayModel);
            new Thread(this.renderThemeFuture).start();
        }


        @Override
        protected RendererJob createJob(Tile tile) {
            return new RendererJob(tile, mapDataStore,
                    renderThemeFuture,
                    model.displayModel,
                    TEXT_SCALE,
                    TRANSPARENT, false);
        }

        @Override
        protected boolean isTileStale(Tile tile, TileBitmap bitmap) {
            return mapDataStore.getDataTimestamp(tile) > bitmap.getTimestamp();
        }






        @Override
        public void draw(BoundingBox b, byte z, Canvas c, Point t) {

        }
    }


    private static class Cache implements TileCache {
        private static class Entry {
            public final MapsForgeTileObject observer;
            public TileBitmap bitmap = null;

            public Entry(MapsForgeTileObject o) {
                observer = o;
            }
        }

        private final SparseArray<Entry> cache = new SparseArray(20);


        @Override
        public boolean containsKey(Job key) {
            return get(key) != null;
        }

        @Override
        public void destroy() {
            purge();
        }

        @Override
        public TileBitmap get(Job key) {
            Entry e = cache.get(toKey(key));
            if (e != null)
                return e.bitmap;
            return null;
        }


        @Override
        public int getCapacity() {
            return cache.size();
        }

        @Override
        public int getCapacityFirstLevel() {
            return getCapacity();
        }

        @Override
        public TileBitmap getImmediately(Job key) {
            return get(key);
        }

        @Override
        public void purge() {
            for (int i = 0; i< cache.size(); i++) {
                Entry e = cache.valueAt(i);
                if (e != null && e.bitmap != null) {
                    e.bitmap.decrementRefCount();
                }
            }
            cache.clear();
        }

        @Override
        public void put(Job key, TileBitmap bitmap) {
            if (bitmap != null) {
                Entry e =  cache.get(toKey(key));

                if (e != null) {
                    bitmap.incrementRefCount();
                    e.bitmap = bitmap;
                    e.observer.onChange();
                }
            }
        }



        public void lockToCache(MapsForgeTileObject t) {
            cache.put(toKey(t), new Entry(t));
            AppLog.d(this, "Size: " + cache.size());
        }


        public void freeFromCache(MapsForgeTileObject t) {
            Entry e = cache.get(toKey(t));

            cache.remove(toKey(t));

            if (e != null && e.bitmap != null) {
                e.bitmap.decrementRefCount();
            }
        }


        private int toKey(MapsForgeTileObject t) {
            return t.getTile().hashCode();
        }


        private int toKey(Job j) {
            return j.tile.hashCode();
        }


        @Override
        public void setWorkingSet(Set<Job> workingSet) {}

        @Override
        public void addObserver(Observer observer) {
            AppLog.d(this, "Use lockToCache()!");
        }

        @Override
        public void removeObserver(Observer observer) {
            AppLog.d(this, "Use freeFromCache()!");
        }
    }



    @Override
    public void appendStatusText(StringBuilder builder) {

    }

    @Override
    public void close() {
        renderer.destroy();
    }
}
