package ch.bailu.aat.services.tileremover

import ch.bailu.foc.Foc

data class SelectedTileDirectoryInfo(
    val directory: Foc,
    val name: String,
    val index: Int
)
