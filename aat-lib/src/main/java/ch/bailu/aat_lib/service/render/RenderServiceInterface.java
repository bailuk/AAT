package ch.bailu.aat_lib.service.render;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.service.cache.ObjTileMapsForge;

public interface RenderServiceInterface {
    void lockToRenderer(ObjTileMapsForge objTileMapsForge);
    void freeFromRenderer(ObjTileMapsForge objTileMapsForge);

    boolean supportsTile(Tile t);
}
