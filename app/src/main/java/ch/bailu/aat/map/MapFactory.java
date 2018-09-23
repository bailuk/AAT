package ch.bailu.aat.map;

import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.EditorSourceInterface;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.layer.CurrentLocationLayer;
import ch.bailu.aat.map.layer.control.CustomBarLayer;
import ch.bailu.aat.map.layer.control.EditorLayer;
import ch.bailu.aat.map.layer.control.InformationBarLayer;
import ch.bailu.aat.map.layer.control.NavigationBarLayer;
import ch.bailu.aat.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat.map.layer.gpx.GpxOverlayListLayer;
import ch.bailu.aat.map.layer.grid.Crosshair;
import ch.bailu.aat.map.layer.grid.GridDynLayer;
import ch.bailu.aat.map.mapsforge.MapsForgeView;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.bar.ControlBar;

public class MapFactory {

    private final MapViewInterface m;
    private final DispatcherInterface d;
    private final MapContext mc;



    public static MapFactory DEF(AbsDispatcher d, String skey) {
        return MF(d.getServiceContext(), d, skey);
    }

    public static MapFactory MF(ServiceContext sc, AbsDispatcher d, String skey) {
        return new MapFactory(new MapsForgeView(sc, d, skey), d);
    }


    public MapFactory(MapViewInterface map, final AbsDispatcher dispatcher) {
        d = dispatcher;
        m = map;
        mc = m.getMContext();
        dispatcher.addLC(m);
    }


    public MapViewInterface base(int size) {
        m.add(new CurrentLocationLayer(mc, d));
        m.add(new NavigationBarLayer(mc, d, size));
        return m;
    }


    public MapViewInterface split() {
        m.add(new CurrentLocationLayer(mc, d));
        m.add(new GpxOverlayListLayer(mc,d));
        m.add(new GpxDynLayer(mc, d, InfoID.EDITOR_DRAFT));
        m.add(new GpxDynLayer(mc, d, InfoID.TRACKER));
        m.add(new Crosshair());

        return m;
    }

    public MapViewInterface tracker(EditorSourceInterface e) {
        return tracker(e, InfoID.EDITOR_DRAFT);
    }

    private MapViewInterface tracker(EditorSourceInterface e, int iid) {
        base(4);
        m.add(new GpxOverlayListLayer(mc,d));
        m.add(new EditorLayer(mc, d, iid, e));
        m.add(new GpxDynLayer(mc, d, InfoID.FILEVIEW));
        m.add(new GpxDynLayer(mc, d, InfoID.TRACKER));
        m.add(new GridDynLayer(mc));
        m.add(new InformationBarLayer(mc, d));

        return m;
    }


    public MapViewInterface map(EditorSourceInterface e, ControlBar b) {
        tracker(e);

        m.add(new CustomBarLayer(mc, b));

        return m;
    }


    public MapViewInterface list(AbsGpxListActivity a) {
        base(4);

        m.add(new GpxOverlayListLayer(mc, d));
        m.add(new GpxDynLayer(mc, d, InfoID.LIST_SUMMARY));
        m.add(new GridDynLayer(mc));
        m.add(new InformationBarLayer(mc, d));
        return m;
    }


    public MapViewInterface editor(EditorSourceInterface e) {
        return tracker(e, InfoID.EDITOR_OVERLAY);
    }


    public MapViewInterface content(EditorSourceInterface e) {
        return editor(e);
    }


    public MapViewInterface node() {
        base(4);

        m.add(new GpxDynLayer(mc, d, InfoID.TRACKER));
        m.add(new GpxDynLayer(mc, d, InfoID.FILEVIEW));
        m.add(new GridDynLayer(mc));

        return m;
    }


    public MapViewInterface externalContent() {
        m.add(new GpxOverlayListLayer(mc, d));
        m.add(new GpxDynLayer(mc, d, InfoID.FILEVIEW));
        m.add(new CurrentLocationLayer(mc, d));
        m.add(new GridDynLayer(mc));
        m.add(new NavigationBarLayer(mc, d));
        m.add(new InformationBarLayer(mc,d));

        return m;
    }
}
