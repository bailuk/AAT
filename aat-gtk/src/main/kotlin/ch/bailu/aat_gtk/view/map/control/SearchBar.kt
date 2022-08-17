package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.ellipsize
import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.search.SearchController
import ch.bailu.aat_gtk.search.SearchModel
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import org.mapsforge.core.model.LatLong

class SearchBar(app: Application, centerMap: (LatLong)-> Unit): Bar(Position.TOP) {
    private val searchModel = SearchModel()
    private val searchController = SearchController(searchModel)

    init {
        searchController.centerMap = centerMap
        box.append(Box(Orientation.HORIZONTAL, 0).apply {
            halign = Align.START
            valign = Align.START
            addCssClass(Strings.linked)
            marginTop = Layout.marginBig
            marginStart = Layout.marginBig

            val entry = SearchEntry()
            append(entry.apply {
                onActivate {
                    searchController.search(Editable(cast()).text.toString())
                }
            })

            append(Button.newFromIconNameButton(Str("edit-find-symbolic")).apply {
                onClicked {
                    searchController.search(Editable(entry.cast()).text.toString())
                }
            })

            append(MenuButton().apply {
                iconName = Str("view-more-symbolic")
                searchModel.observe {
                    menuModel = MenuModelBuilder().apply {
                        it.forEach { name, latLong ->
                            label(name.ellipsize(30)) {
                                searchController.centerMap(latLong)
                            }
                        }
                    }.create(app)
                }
            })
        })
    }
}
