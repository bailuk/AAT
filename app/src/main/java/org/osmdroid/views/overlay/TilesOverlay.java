package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.util.MyMath;
import org.osmdroid.views.MapView;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.views.map.AbsTileProvider;
import microsoft.mappoint.TileSystem;


public class TilesOverlay extends Overlay  {


	/** Current tile source */
	protected final AbsTileProvider mTileProvider;

	/* to avoid allocations during draw */
	protected final Paint mPaint = new Paint();
	private final Rect mTileRect = new Rect();
	private final Rect mViewPort = new Rect();

	private int mWorldSize_2;

	public TilesOverlay(final AbsTileProvider aTileProvider) {
		mTileProvider = aTileProvider;
	}

	@Override
	public void onDetach(final MapView pMapView) {
		this.mTileProvider.detach();
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
		final MapView.Projection pj = osmv.getProjection();
		final int zoomLevel = pj.getZoomLevel();
		mWorldSize_2 = osmv.tileSystem.MapSize(zoomLevel) / 2;

		// Get the area we are drawing to
		mViewPort.set(pj.getScreenRect());

		// Translate the Canvas coordinates into Mercator coordinates
		mViewPort.offset(mWorldSize_2, mWorldSize_2);

		// Draw the tiles!
		drawTiles(c, pj.getZoomLevel(), osmv.tileSystem, mViewPort, osmv.getContext());
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
		Point upperLeft = tileSystem.PixelXYToTileXY(viewPort.left, viewPort.top, null);
		upperLeft.offset(-1, -1);
		Point lowerRight = tileSystem.PixelXYToTileXY(viewPort.right, viewPort.bottom, null);

		final int mapTileUpperBound = 1 << zoomLevel;

		// make sure the cache is big enough for all the tiles
		final int numNeeded = (lowerRight.y - upperLeft.y + 1) * (lowerRight.x - upperLeft.x + 1);
		mTileProvider.ensureCapacity(numNeeded);

		/* Draw all the MapTiles (from the upper left to the lower right). */
		for (int y = upperLeft.y; y <= lowerRight.y; y++) {
			for (int x = upperLeft.x; x <= lowerRight.x; x++) {
				// Construct a MapTile to Request from the tile provider.
				final int tileY = MyMath.mod(y, mapTileUpperBound);
				final int tileX = MyMath.mod(x, mapTileUpperBound);
				final MapTile tile = new MapTile(zoomLevel, tileX, tileY);

				Drawable currentMapTile = mTileProvider.getMapTile(tile);
				if (currentMapTile == null) {
					currentMapTile = LoadingTile.get(context);
				}

				if (currentMapTile != null) {
					mTileRect.set(x * tileSizePx, y * tileSizePx, x * tileSizePx + tileSizePx, y
							* tileSizePx + tileSizePx);
					onTileReadyToDraw(c, currentMapTile, mTileRect);
				}

			}
		}

	}

	protected void onTileReadyToDraw(final Canvas c, final Drawable currentMapTile,
			final Rect tileRect) {
		tileRect.offset(-mWorldSize_2, -mWorldSize_2);
		currentMapTile.setBounds(tileRect);
		currentMapTile.draw(c);
	}
}
