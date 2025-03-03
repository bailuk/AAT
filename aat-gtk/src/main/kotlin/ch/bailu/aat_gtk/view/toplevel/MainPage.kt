package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.menu.MainMenuButton
import ch.bailu.aat_gtk.view.toplevel.list.FileListPage
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.Clamp
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation

class MainPage(appContext: AppContext,
               controller: UiControllerInterface,
               app: Application, window: ApplicationWindow,
               dispatcher: Dispatcher,
               usageTrackers: UsageTrackers) {

    private val showMapButton = Button().apply {
        setLabel(Res.str().p_map())
        onClicked {
            controller.showMap()
        }
    }

    private val detailViewPage = DetailViewPage(controller, dispatcher, usageTrackers.createSelectableUsageTracker())

    private val headerBar = HeaderBar().apply {
        titleWidget = WindowTitle(GtkAppConfig.appName, GtkAppConfig.appLongName)
        packEnd(showMapButton)
        packStart(MainMenuButton(window, dispatcher, controller).apply {
            createActions(app)
        }.menuButton)
    }

    val stackView = StackView(SOLID_KEY).apply {
        addView(CockpitPage(appContext,controller, dispatcher).box, pageIdCockpit, Res.str().intro_cockpit())
        addView(FileListPage(app, appContext, controller).vbox, pageIdFileList, Res.str().label_list())
        addView(detailViewPage.box, pageIdDetail, Res.str().label_detail())
        restore(appContext.storage)
    }

    val layout = Box(Orientation.VERTICAL, 0).apply {
            append(headerBar)
            append(stackView.viewSwitcherBar)
            append(Clamp().apply {
                maximumSize = Layout.stackWidth
                child = stackView.viewStack
            })
    }

    fun showCockpit() {
        stackView.showPage(pageIdCockpit)
    }

    fun showDetail() {
        stackView.showPage(pageIdDetail)
    }

    fun showFileList() {
        stackView.showPage(pageIdFileList)
    }

    fun showInDetail(iid: Int) {
        detailViewPage.select(iid)
    }

    companion object {
        private const val SOLID_KEY = "StackPage"

        val pageIdCockpit  = Icons.incCockpitSymbolic
        val pageIdFileList = Icons.viewListSymbolic
        val pageIdDetail   = Icons.viewContinuousSymbolic
    }
}
