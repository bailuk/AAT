package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.tileprovider.MapTile;

public class XYTileSource extends OnlineTileSourceBase {

	public XYTileSource(final String aName, final int aZoomMinLevel,
			final int aZoomMaxLevel, final String aImageFilenameEnding,
			final String... aBaseUrl) {
		super(aName, aZoomMinLevel, aZoomMaxLevel,
				aImageFilenameEnding, aBaseUrl);
	}

	@Override
	public String getTileURLString(final MapTile aTile) {
		return getBaseUrl() + aTile.getZoomLevel() + "/" + aTile.getX() + "/" + aTile.getY()
				+ mImageFilenameEnding;
	}
}
