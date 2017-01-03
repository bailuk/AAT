package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.util.MyMath;
import org.osmdroid.views.MapView;

import ch.bailu.aat.map.osm.AbsTileProvider;
import microsoft.mappoint.TileSystem;


public class TilesOverlay extends Overlay  {


	/** Current tile source */
	private final AbsTileProvider mTileProvider;

	/* to avoid allocations during draw */
	private final Rect mTileRect = new Rect();
	private final Rect mViewPort = new Rect();
    private final Point mUpperLeft = new Point();
    private final Point mLowerRight = new Point();

	private int mWorldSize_2;

	public TilesOverlay(final AbsTileProvider aTileProvider) {
		mTileProvider = aTileProvider;
	}

	@Override
	public void onDetach(final MapView pMapView) {
		this.mTileProvider.detach();
	}

	@Override
	public void onAttach(final MapView pMapView) {
		this.mTileProvider.attach();
	}

	public int getMinimumZoomLevel() {
		return mTileProvider.getMinimumZoomLevel();
	}
	public int getMaximumZoomLevel() {
		return mTileProvider.getMaximumZoomLevel();
	}


	@Override
	protected void draw(final Canvas c, final MapView osmv) {
		
		mTileProvider.setStartTime();
		// Calculate the half-world size
		final MapView.Projection projection = osmv.getProjection();
		final int zoomLevel = projection.getZoomLevel();
		mWorldSize_2 = osmv.tileSystem.MapSize(zoomLevel) / 2;

		// Get the area we are drawing to
		mViewPort.set(projection.getScreenRect());

		// Translate the Canvas coordinates into Mercator coordinates
		mViewPort.offset(mWorldSize_2, mWorldSize_2);

		// Draw the tiles!
		drawTiles(c, projection.getZoomLevel(), osmv.tileSystem, mViewPort, osmv.getContext());
	}

	/**
	 * This is meant to be a "pure" tile drawing function that doesn't take into account
	 * platform-specific characteristics (like Android's canvas's having 0,0 as the center rather
	 * than the upper-left corner).
	 */


	public void drawTiles(final Canvas c, final int zoomLevel, final TileSystem tileSystem,
			final Rect viewPort, final Context context) {

	    final int tileSizePx = tileSystem.getTileSize();
		
	
		// Calculate the amount of tiles needed for each side around the center one.
		tileSystem.PixelXYToTileXY(viewPort.left, viewPort.top, mUpperLeft);
		mUpperLeft.offset(-1, -1);
		tileSystem.PixelXYToTileXY(viewPort.right, viewPort.bottom, mLowerRight);

		final int mapTileUpperBound = 1 << zoomLevel;

		// make sure the cache is big enough for all the tiles
		final int capacity = (mLowerRight.y - mUpperLeft.y + 1) * (mLowerRight.x - mUpperLeft.x + 1);
		mTileProvider.ensureCapacity(capacity);

		/* Draw all the MapTiles (from the upper left to the lower right). */
		for (int y = mUpperLeft.y; y <= mLowerRight.y; y++) {
			for (int x = mUpperLeft.x; x <= mLowerRight.x; x++) {
				// Construct a MapTile to Request from the tile provider.
				final int tileY = MyMath.mod(y, mapTileUpperBound);
				final int tileX = MyMath.mod(x, mapTileUpperBound);
				final MapTile tile = new MapTile(zoomLevel, tileX, tileY);

				Drawable currentMapTile = mTileProvider.getMapTile(tile);
				if (currentMapTile == null) {
					currentMapTile = LoadingTile.getDrawable(context);
				}

				if (currentMapTile != null) {
					mTileRect.set(x * tileSizePx, y * tileSizePx, x * tileSizePx + tileSizePx, y
							* tileSizePx + tileSizePx);
					drawTile(c, currentMapTile, mTileRect);
				}
			}
		}
	}



    private void drawTile(final Canvas c, final Drawable currentMapTile,
                          final Rect tileRect) {
		tileRect.offset(-mWorldSize_2, -mWorldSize_2);
		currentMapTile.setBounds(tileRect);
		currentMapTile.draw(c);
	}
}
