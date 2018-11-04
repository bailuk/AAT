package ch.bailu.aat.map.tile;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.util.ui.AppLog;

public class AlternativeTile {

    private final Matrix matrix;
    private final TileProvider provider;


    public AlternativeTile(TileProvider p) {
        matrix = AndroidGraphicFactory.INSTANCE.createMatrix();
        provider = p;
    }

    public void drawParentTileBitmap(Canvas canvas, Point point, Tile tile,
                                      int tileSize) {
        Tile parentTile = tile.getParent();
        if (parentTile != null) {
            final TileBitmap bitmap = provider.get(parentTile);
            if (bitmap != null) {

                AppLog.d(this, "Draw alternative bitmap");
                long translateX = tile.getShiftX(parentTile) * tileSize;
                long translateY = tile.getShiftY(parentTile) * tileSize;
                byte zoomLevelDiff = (byte) (tile.zoomLevel - parentTile.zoomLevel);
                float scaleFactor = (float) Math.pow(2, zoomLevelDiff);

                int x = (int) Math.round(point.x);
                int y = (int) Math.round(point.y);

                matrix.reset();
                matrix.translate(x - translateX, y - translateY);
                matrix.scale(scaleFactor, scaleFactor);

                canvas.setClip(x, y, tileSize, tileSize);

                canvas.drawBitmap(bitmap, matrix, Filter.NONE);
                canvas.resetClip();
            }
        }
    }




}
