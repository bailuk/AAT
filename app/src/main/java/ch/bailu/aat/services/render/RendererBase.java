package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.layer.queue.JobQueue;
import org.mapsforge.map.model.Model;

public abstract class RendererBase<T extends Job> extends Layer {
    private final static boolean HAS_JOB_QUEUE = true;

    protected final JobQueue<T> jobQueue;

    private final TileCache cache;

    public RendererBase(TileCache c, Model model) {
        cache = c;
        displayModel=model.displayModel;
        jobQueue = new JobQueue<>(model.mapViewPosition, model.displayModel);
    }

    public TileBitmap getTile(Tile tile) {
        T job = createJob(tile);
        TileBitmap bitmap = cache.getImmediately(job);

        if (bitmap == null) {
            if (HAS_JOB_QUEUE && !cache.containsKey(job)) {
                jobQueue.add(job);
            }

        } else {
            if (isTileStale(tile, bitmap) && HAS_JOB_QUEUE && !cache.containsKey(job)) {
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

    protected void retrieveLabelsOnly(T job) {}

}
