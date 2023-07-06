package ch.bailu.aat_lib.app

import org.mapsforge.core.graphics.GraphicFactory

object AppGraphicFactory {
    private var factory: GraphicFactory? = null

    fun set(factory: GraphicFactory) {
        AppGraphicFactory.factory = factory
    }

    @JvmStatic
    fun instance(): GraphicFactory {
        return this.factory ?: throw RuntimeException(AppGraphicFactory::class.java.simpleName + " is not initialized")
    }
}
