package ch.bailu.aat.services.cache;

import java.util.ArrayList;

import microsoft.mappoint.TileSystem;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.util.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;
import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.dem.Dem3Tile;
import ch.bailu.aat.services.dem.DemDimension;
import ch.bailu.aat.services.dem.DemGeoToIndex;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.services.dem.DemSplitter;
import ch.bailu.aat.services.dem.ElevationUpdaterClient;

public abstract class ElevationTile extends TileObject implements ElevationUpdaterClient{

    private final MapTile map_tile;
    private final int split;
    
    private boolean updateLock=false;
    
    
    private final SynchronizedBitmap bitmap=new SynchronizedBitmap();

    private final SparseArray<TilePainter> tilePainterList = new SparseArray<>(25);
    
    private final int[] toLaRaster = new int[TileObject.TILE_SIZE]; // 1. pixel to latitude | 2. pixel to dem index 
    private final int[] toLoRaster = new int[TileObject.TILE_SIZE];

    

    
    public ElevationTile(String id, ServiceContext cs, MapTile _map_tile, int _split) {
        super(id);
        map_tile=_map_tile;
        split=_split;
    }

    
    
    public DemProvider split(DemProvider dem) {
        int i=split;
        while(i>0) {
            dem=factorySplitter(dem);
            i--;
        }
        return dem;
    }

    public DemProvider factorySplitter(DemProvider dem) {
        return DemSplitter.factory(dem);
    }

    public DemGeoToIndex factoryGeoToIndex(DemDimension dim) {
        return new DemGeoToIndex(dim);
    }
    
    
    
    public abstract void fillBitmap(
            int[] bitmap, 
            int[] toLaRaster, 
            int[] toLoRaster, 
            Span laSpan, 
            Span loSpan, 
            DemProvider demtile);

    
    @Override
    public void onInsert(ServiceContext sc) {
        sc.getCacheService().addToBroadcaster(this);
        sc.getBackgroundService().process(new SrtmTileRasterInitializer());
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}



    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {
        if (haveID(url)) {
            AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.REQUEST_ELEVATION_UPDATE, toString());
        }
    }



    private boolean haveID(String id) {
        for (int i=0; i<tilePainterList.size(); i++) {
            if (id.contains(tilePainterList.valueAt(i).toString())) {
                return true;
            }
        }
        return false;
    }



    @Override
    public Bitmap getBitmap() {
        return bitmap.get();
    }


    @Override
    public boolean isReady() {
        return updateLock == false;
    }


    /*
    @Override
    public boolean isUpdating() {
        return updateLock;
    }
*/

    @Override
    public SrtmCoordinates[] getSrtmTileCoordinates() {
        final SrtmCoordinates c[] = new SrtmCoordinates[tilePainterList.size()];

        for (int i=0; i<tilePainterList.size(); i++) {
            c[i]=tilePainterList.valueAt(i).coordinates;
        }
        return c;
    }



    @Override
    public void updateFromSrtmTile(ServiceContext cs, Dem3Tile tile) {
        final int key = tile.hashCode();
        final TilePainter painter =  tilePainterList.get(key);

        if (painter != null && tile.isLoaded()) {
            tilePainterList.remove(key);
            updateLock=true;
            painter.setTile(tile);
            cs.getBackgroundService().process(painter);
        }
    }


    private class SrtmTileRasterInitializer extends ProcessHandle {

        final private ArrayList<Span> laSpan = new ArrayList<>(5);
        final private ArrayList<Span> loSpan = new ArrayList<>(5);


        @Override
        public long bgOnProcess() {
            initializeWGS84Raster();
            initializeIndexRaster();
            generateTilePainterList();

            return TileObject.TILE_SIZE*2;
        }

        private void initializeWGS84Raster() {
            final Rect tileR = getTileR();

            final GeoPoint tl=pixelToGeo(
                    tileR.left, 
                    tileR.top);

            final GeoPoint br=pixelToGeo(
                    tileR.right, 
                    tileR.bottom);

            final int laDiff=br.getLatitudeE6()-tl.getLatitudeE6();
            final int loDiff=br.getLongitudeE6()-tl.getLongitudeE6(); //-1.2 - -1.5 = 0.3  //1.5 - 1.2 = 0.3


            int la = tl.getLatitudeE6();
            int lo = tl.getLongitudeE6();

            final Span laS=new Span((int) Math.floor(la/1e6d));
            final Span loS=new Span((int) Math.floor(lo/1e6d));

            final int laInc=laDiff / TileObject.TILE_SIZE;
            final int loInc=loDiff / TileObject.TILE_SIZE;
            
            int i;
            for (i=0; i< TileObject.TILE_SIZE; i++) {
                toLaRaster[i]=la;   
                toLoRaster[i]=lo;

                final int laDeg = (int) Math.floor(la/1e6d);
                final int loDeg = (int) Math.floor(lo/1e6d);

                laS.takeSpan(laSpan, i, laDeg);
                loS.takeSpan(loSpan, i, loDeg);
                
                la+=laInc;
                lo+=loInc;
            }


            laS.takeSpan(laSpan,i);
            loS.takeSpan(loSpan,i);
        }

 
        private GeoPoint pixelToGeo(int x, int y) {
            return TileSystem.PixelXYToLatLong(
                    x, 
                    y,
                    map_tile.getZoomLevel(), 
                    TileObject.TILE_SIZE, 
                    null);
        }        
        
        private void initializeIndexRaster() {
            DemDimension dim=split(Dem3Tile.NULL).getDim();
            

            DemGeoToIndex toIndex=factoryGeoToIndex(dim);
            
            
            for (int i=0; i< TileObject.TILE_SIZE; i++) {
                toLaRaster[i]=toIndex.toYPos(toLaRaster[i]);  
                toLoRaster[i]=toIndex.toXPos(toLoRaster[i]);
            }
        }


        private void generateTilePainterList() {
            for (int la=0; la<laSpan.size(); la++) {
                for (int lo=0; lo<loSpan.size(); lo++) {
                    putTilePainter(laSpan.get(la), loSpan.get(lo));
                }
            }
        }


        private void putTilePainter(Span la, Span lo) {
            TilePainter painter = new TilePainter(la, lo);
            tilePainterList.put(painter.hashCode(), painter);
        }

        private Rect getTileR() {
            final Point tileTL = tileToPixel();

            final Point tileBR = new Point(
                    tileTL.x+TileObject.TILE_SIZE, 
                    tileTL.y+TileObject.TILE_SIZE);



            return new Rect(tileTL.x, tileTL.y, tileBR.x, tileBR.y);
        }


        private Point tileToPixel() {
            return new Point(map_tile.getX()*TileObject.TILE_SIZE, map_tile.getY()*TileObject.TILE_SIZE);
        }



 

        @Override
        public void broadcast(Context context) {
            if (tilePainterList.size()>0) {
                AppBroadcaster.broadcast(context, AppBroadcaster.REQUEST_ELEVATION_UPDATE, ElevationTile.this.toString());
            }
        }
    }


    private static final int[] buffer = new int[TileObject.TILE_SIZE*TileObject.TILE_SIZE];


    private class TilePainter extends ProcessHandle {
        private Dem3Tile tile=null;
        private final SrtmCoordinates coordinates;


        private final Span laSpan;
        private final Span loSpan;

        public TilePainter(Span laS, Span loS) {
            coordinates = new SrtmCoordinates((double)laS.deg(), (double)loS.deg());

            laSpan = laS;
            loSpan = loS;
        }

        public void setTile(Dem3Tile t) {
            tile = t;
            tile.lock();
        }

        @Override
        public String toString() {
            return coordinates.toString();
        }


        @Override
        public int hashCode() {
            return toString().hashCode();
        }





        @Override
        public long bgOnProcess() {
            final Rect interR = Span.toRect(laSpan, loSpan);

            fillBitmap(buffer,toLaRaster, toLoRaster, laSpan, loSpan, split(tile));


            Bitmap tile = bitmap.get();

            if (tile == null) {
                tile = SynchronizedBitmap.createBitmap(TILE_SIZE, TILE_SIZE);
                tile.eraseColor(Color.TRANSPARENT);
            }

            tile.setPixels(
                    buffer, 
                    0, 
                    interR.width(), 
                    interR.left, 
                    interR.top, 
                    interR.width(), 
                    interR.height());


            bitmap.set(tile);
            updateLock=false;

            return interR.width()*interR.height()*2;
        }




        @Override
        public void broadcast(Context context) {
            tile.free();
            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, ElevationTile.this.toString());
        }

    }



}
