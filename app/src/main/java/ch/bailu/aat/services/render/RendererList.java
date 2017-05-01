package ch.bailu.aat.services.render;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

import java.io.File;
import java.util.ArrayList;

import ch.bailu.aat.util.ui.AppLog;

public class RendererList {
    private final static int LIMIT=5;

    private final ArrayList<Entry> renderer = new ArrayList(LIMIT);
    private final TileCache cache;
    private final RenderThemeFuture theme;

    public RendererList(TileCache c, XmlRenderTheme xmlTheme) {

        AppLog.d(this, xmlTheme.toString());

        cache = c;
        theme = new RenderThemeFuture(
                AndroidGraphicFactory.INSTANCE,   // TODO: move to context
                xmlTheme,
                new Model().displayModel);        // TODO: move to context
        new Thread(theme).start();
    }


    private class Entry {
        public final Renderer renderer;
        public final ArrayList<File> files;
        private long stamp;


        public Entry(ArrayList<File> f) {
            files = f;
            renderer = new Renderer(theme, cache, files);
            use();
        }

        public void use() {
            stamp = System.currentTimeMillis();
        }

        public long getStamp() {
            return stamp;
        }

        public boolean hasSameFiles(ArrayList<File> files) {
            if (files.size() == this.files.size()) {
                for (File f: files) {
                    if (hasFile(f) == false) return false;
                }
                return true;
            }
            return false;
        }

        private boolean hasFile(File f) {
            for (File b: files) {
                if (b.equals(f)) return true;
            }
            return false;
        }
    }


    public TileBitmap getTile(ArrayList<File> files, Tile tile) {
        if (files.size() > 0) {
            Renderer r = get(files);
            if (r != null) {
                return r.getTile(tile);
            }
        }
        return null;
    }


    private Renderer get(ArrayList<File> files) {

        for (Entry e: renderer) {
            if (e.hasSameFiles(files)) {
                return e.renderer;
            }
        }

        AppLog.d(this, "add_w renderer");
        Entry e = add(files);
        e.use();
        return e.renderer;
    }

    private Entry add(ArrayList<File> files) {
        if (renderer.size() == LIMIT) {
            removeOldest();
        }
        Entry e = new Entry(files);
        renderer.add(e);
        return e;
    }


    private void removeOldest() {
        int toRemove =0;

        for (int i = 1; i< renderer.size(); i++) {
            if (renderer.get(i).getStamp() < renderer.get(toRemove).getStamp()) {
                toRemove = i;
            }

        }

        renderer.get(toRemove).renderer.destroy();
        renderer.remove(toRemove);
    }


    public void destroy() {
        for (Entry e: renderer) {
            e.renderer.destroy();
        }
        renderer.clear();
        theme.decrementRefCount();
    }
}
