package ch.bailu.aat.services.cache;


import android.content.Context;
import android.graphics.Bitmap;

import org.osmdroid.tileprovider.MapTile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.file.FileAccess;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;

public class HillShadeCached extends TileObject {

    private final FileHandle save;

    private TileObject tile = null;

    private final ObjectHandle.Factory bitmapFactory, demFactory;
    private final String bitmapID, demID;



    public HillShadeCached(String id, ServiceContext cs,  MapTile t) {
        super(id);



        demID = NewHillshade.ELEVATION_HILLSHADE8.getID(t, cs.getContext());
        demFactory = NewHillshade.ELEVATION_HILLSHADE8.getFactory(t);

        bitmapID = BitmapTileObject.HILLSHADE_CACHE.getID(t, cs.getContext());
        bitmapFactory = BitmapTileObject.HILLSHADE_CACHE.getFactory(t);

        cs.getCacheService().addToBroadcaster(this);


        save = new FileHandle(id) {

            @Override
            public long bgOnProcess() {

                OutputStream out = null;

                try {
                    out = FileAccess.openOutput(new File(bitmapID));
                    tile.getBitmap().compress(Bitmap.CompressFormat.PNG, 0, out);

                } catch (IOException e) {
                    e.printStackTrace();


                } finally {
                    FileAccess.close(out);
                }
                return tile.getSize();
            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_ONDISK,
                        bitmapID, demID);
            }
        };

    }


    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable()) {
            tile = (TileObject) sc.getCacheService().getObject(bitmapID, bitmapFactory);
        } else {
            tile = (TileObject) sc.getCacheService().getObject(demID, demFactory);
        }
    }


    @Override
    public void onRemove(ServiceContext cs) {
        tile.free();
    }


    @Override
    public void reDownload(ServiceContext sc) {
        toFile().delete();
        tile.free();
        tile = (TileObject) sc.getCacheService().getObject(bitmapID, bitmapFactory);
    }


    @Override
    public boolean isLoaded() {
        return tile.isLoaded();
    }



    @Override
    public File toFile() {
        return new File(bitmapID);
    }



    private boolean isLoadable() {
        return toFile().exists();
    }


    @Override
    public void onDownloaded(String id, String u, ServiceContext sc) {}


    @Override
    public void onChanged(String id, ServiceContext sc) {
        // FIXME gets called when tile is NULL
        if (id.equals(tile.toString())) {
            AppBroadcaster.broadcast(sc.getContext(),
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    toString());


            if (id.equals(demID) && tile.isLoaded() && toFile().exists()==false) {
                sc.getBackgroundService().process(save);
            }
        }
    }



    @Override
    public boolean isReady() {
        return tile.isReady();
    }


    @Override
    public long getSize() {
        return TileObject.MIN_SIZE;
    }

    @Override
    public Bitmap getBitmap() {
        return tile.getBitmap();
    }



    public static class Factory extends ObjectHandle.Factory {
        private final MapTile mapTile;


        public Factory(MapTile mt) {
            mapTile=mt;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new HillShadeCached(id, cs, mapTile);
        }
    }


    public final static TileObject.Source ELEVATION_HILLSHADE_CACHED =
            new TileObject.Source() {

                @Override
                public String getName() {
                    return "Hillshade Cached*";
                }

                @Override
                public String getID(MapTile t, Context x) {
                    return HillShadeCached.class.getSimpleName() +
                            "/" + t.getZoomLevel() + "/" + t.getX() + "/" + t.getY();
                }

                @Override
                public int getMinimumZoomLevel() {
                    return NewHillshade.ELEVATION_HILLSHADE8.getMinimumZoomLevel();
                }

                @Override
                public int getMaximumZoomLevel() {
                    return NewHillshade.ELEVATION_HILLSHADE8.getMaximumZoomLevel();
                }

                @Override
                public ObjectHandle.Factory getFactory(MapTile mt) {
                    return  new HillShadeCached.Factory(mt);
                }

                @Override
                public TileBitmapFilter getBitmapFilter() {
                    return TileBitmapFilter.COPY_FILTER;
                }
            };
}
