package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.gtk.gtk.Application

interface ActionProviderInterface {
    fun createActions(app: Application)
}
