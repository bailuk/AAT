package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.ActionProviderInterface
import ch.bailu.gtk.type.Str

interface MenuButtonInterface : ActionProviderInterface{
    fun setIcon(iconName: String)
    fun setIcon(iconName: Str)
}
