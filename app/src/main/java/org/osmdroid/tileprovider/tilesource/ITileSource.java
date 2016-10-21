package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.tileprovider.MapTile;

public interface ITileSource {


	/**
	 * A human-friendly name for this tile source
	 * 
	 * @return the tile source name
	 */
	String name();


	/**
	 * Get a unique file path for the tile. This file path may be used to store the tile on a file
	 * system and performance considerations should be taken into consideration. It can include
	 * multiple paths. It should not begin with a leading path separator.
	 * 
	 * @param aTile
	 *            the tile
	 * @return the unique file path
	 */
	String getTileRelativeFilenameString(MapTile aTile);

	/**
	 * Get the minimum zoom level this tile source can provide.
	 * 
	 * @return the minimum zoom level
	 */
	public int getMinimumZoomLevel();

	/**
	 * Get the maximum zoom level this tile source can provide.
	 * 
	 * @return the maximum zoom level
	 */
	public int getMaximumZoomLevel();

}
