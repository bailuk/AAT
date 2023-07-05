package ch.bailu.aat.views.graph

import android.content.Context
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.PercentageLayout
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.view.graph.DistanceAltitudePlotter
import ch.bailu.aat_lib.view.graph.DistanceSpeedPlotter
import ch.bailu.aat_lib.view.graph.Plotter
import ch.bailu.aat_lib.view.graph.SpmPlotter

object GraphViewFactory {
    fun speed(
        appContext: AppContext, context: Context, dispatcher: DispatcherInterface,
        theme: UiTheme, vararg iid: Int
    ): PercentageLayout {
        return PercentageLayout(context).add(
            createSpeedGraph(appContext, context, theme).connect(dispatcher, *iid), 100
        )
    }

    fun all(
        appContext: AppContext,
        context: Context,
        dispatcher: DispatcherInterface,
        theme: UiTheme,
        vararg iid: Int
    ): PercentageLayout {
        return PercentageLayout(context, 0).add(
            createAltitudeGraph(appContext, context, theme).connect(
                dispatcher, *iid
            ).hideXLabel(), 33
        ).add(
            createSpeedGraph(appContext, context, theme).connect(
                dispatcher, *iid
            ).hideXLabel(), 33
        ).add(
            createSpmGraph(appContext, context, theme).connect(
                dispatcher, *iid
            ), 33
        )
    }

    fun createSpeedGraph(appContext: AppContext, context: Context, theme: UiTheme): GraphView {
        val plotter: Plotter = DistanceSpeedPlotter(SolidUnit(appContext.storage))
        return GraphView(context, appContext, plotter, theme)
    }

    fun createSpmGraph(appContext: AppContext, context: Context, theme: UiTheme): GraphView {
        val plotter: Plotter = SpmPlotter(SolidUnit(appContext.storage))
        return GraphView(context, appContext, plotter, theme)
    }

    fun createAltitudeGraph(
        appContext: AppContext,
        context: Context,
        theme: UiTheme
    ): GraphView {
        val plotter: Plotter = DistanceAltitudePlotter(SolidUnit(appContext.storage))
        return GraphView(context, appContext, plotter, theme)
    }
}
