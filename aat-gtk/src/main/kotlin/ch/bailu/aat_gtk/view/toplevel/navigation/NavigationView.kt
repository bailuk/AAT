package ch.bailu.aat_gtk.view.toplevel.navigation

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.Breakpoint
import ch.bailu.gtk.adw.BreakpointBin
import ch.bailu.gtk.adw.BreakpointCondition
import ch.bailu.gtk.adw.BreakpointConditionLengthType
import ch.bailu.gtk.adw.NavigationPage
import ch.bailu.gtk.adw.NavigationSplitView
import ch.bailu.gtk.gtk.PositionType
import ch.bailu.gtk.gtk.Unit
import ch.bailu.gtk.gtk.Widget

class NavigationView(applicationWindow: ApplicationWindow) {

    private val observers = ArrayList<NavigationViewChanged>()

    val leftCollapsed: Boolean
        get() = navigationSplitViewL1.collapsed

    val rightCollapsed: Boolean
        get() = navigationSplitViewL2.collapsed

    private val navigationSplitViewL2 = NavigationSplitView().apply {
        sidebarWidthUnit = Unit.POINTS
        maxSidebarWidth = SIDEBAR_WIDTH.toDouble()
        try {
            sidebarPosition = PositionType.RIGHT
        } catch (e: UnsatisfiedLinkError) {
            AppLog.w(this, "Adw->NavigationSplitView->get_sidebar_position: Available since: 1.7")
        }
    }

    val navigationSplitViewL1 = NavigationSplitView().apply {
        hexpand = true
        vexpand = true
        val breakpoint = Breakpoint(
            BreakpointCondition.newLengthBreakpointCondition(
                BreakpointConditionLengthType.MAX_WIDTH,
                SIDEBAR_WIDTH.toDouble(),
                Unit.POINTS
            )
        )
        breakpoint.onApply {
            this.collapsed = true
            observers.forEach {it.onNavigationViewChanged(this@NavigationView)}
        }
        breakpoint.onUnapply {
            this.collapsed = false
            observers.forEach {it.onNavigationViewChanged(this@NavigationView)}
        }
        applicationWindow.addBreakpoint(breakpoint)

        content = createRightPage()
        sidebarWidthUnit = Unit.POINTS
        maxSidebarWidth = SIDEBAR_WIDTH.toDouble()
    }

    fun observe(navigationViewChanged: NavigationViewChanged) {
        observers.add(navigationViewChanged)
        navigationViewChanged.onNavigationViewChanged(this@NavigationView)
    }

    private fun createRightPage(): NavigationPage {
        val breakpointBin = BreakpointBin()
        val breakpoint = Breakpoint(
            BreakpointCondition.newLengthBreakpointCondition(
                BreakpointConditionLengthType.MAX_WIDTH,
                SIDEBAR_WIDTH.toDouble() * 1.5,
                Unit.POINTS
            )
        )

        // To disable Adwaita-WARNING
        breakpointBin.setIntProperty("width-request", 100)
        breakpointBin.setIntProperty("height-request", 100)

        breakpoint.onApply {
            navigationSplitViewL2.collapsed = true
            observers.forEach {it.onNavigationViewChanged(this@NavigationView)}
        }
        breakpoint.onUnapply {
            navigationSplitViewL2.collapsed = false
            observers.forEach {it.onNavigationViewChanged(this@NavigationView)}
        }

        breakpointBin.addBreakpoint(breakpoint)

        breakpointBin.child = navigationSplitViewL2
        return NavigationPage(breakpointBin, "").apply { canPop = false }
    }

    fun showLeftSidebar() {
        navigationSplitViewL1.showContent = false
    }

    fun showRightSidebar() {
        navigationSplitViewL1.showContent = true
        navigationSplitViewL2.showContent = false

    }

    fun showContent() {
        navigationSplitViewL1.showContent = true
        navigationSplitViewL2.showContent = true
    }

    fun setLeftSidebar(widget: Widget, title: String) {
        navigationSplitViewL1.sidebar = NavigationPage(widget, title).apply { canPop = false }
    }

    fun setRightSidebar(widget: Widget, title: String) {
        navigationSplitViewL2.sidebar = NavigationPage(widget, title).apply { canPop = false }
    }

    fun setContent(widget: Widget, title: String) {
        navigationSplitViewL2.content = NavigationPage(widget, title).apply { canPop = false }
    }

    companion object {
        const val SIDEBAR_WIDTH = 400
    }
}
