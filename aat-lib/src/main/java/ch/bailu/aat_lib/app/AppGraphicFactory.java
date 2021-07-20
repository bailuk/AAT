package ch.bailu.aat_lib.app;

import org.mapsforge.core.graphics.GraphicFactory;

public class AppGraphicFactory {
    private static GraphicFactory factory;

    public static void set(GraphicFactory factory) {
        AppGraphicFactory.factory = factory;
    }

    public static GraphicFactory instance() {
        if (factory == null) {
            throw new RuntimeException(AppGraphicFactory.class.getSimpleName() + " is not initialized");
        }
        return factory;
    }
}
