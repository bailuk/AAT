package ch.bailu.aat_lib.preferences.map

import ch.bailu.foc.Foc

/**
 * Abstract factory class for map specific
 * configuration
 */
interface MapDirectories {
    fun getWellKnownMapDirs(): ArrayList<Foc>
    fun getDefault(): Foc?
    fun createSolidDirectory(): SolidMapsForgeDirectory
    fun createSolidFile(): SolidMapsForgeMapFile
    fun createSolidRenderTheme(): SolidRenderTheme
}
