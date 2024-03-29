package ch.bailu.aat.map.layer

import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.map.MapColor
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position

class CustomBarLayer(mc: MapContext, b: ControlBar, theme: UiTheme) : ControlBarLayer(
    mc,
    b,
    Position.TOP,
    MapColor.setAlpha(theme.getBackgroundColor(), MapColor.ALPHA_LOW)
) {
    init {
        showBar()
    }

    override fun drawInside(mcontext: MapContext) {}
    override fun onAttached() {}
    override fun onDetached() {}
}
