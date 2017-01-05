package ch.bailu.aat.map;

import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.layer.CurrentLocationLayer;
import ch.bailu.aat.map.layer.control.CustomBarLayer;
import ch.bailu.aat.map.layer.control.EditorLayer;
import ch.bailu.aat.map.layer.control.InformationBar;
import ch.bailu.aat.map.layer.control.NavigationBar;
import ch.bailu.aat.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat.map.layer.gpx.GpxOverlayListLayer;
import ch.bailu.aat.map.layer.grid.GridDynLayer;
import ch.bailu.aat.map.mapsforge.MapsForgeView;
import ch.bailu.aat.map.osmdroid.NewOsmInteractiveView;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.ControlBar;

public class MFactory {

    private final MapViewInterface m;
    private final DispatcherInterface d;
    private final MapContext mc;



    public static MFactory DEF(AbsDispatcher d, String skey) {
        return OSM(d.getServiceContext(),d,skey);
    }

    public static MFactory MF(ServiceContext sc, AbsDispatcher d, String skey) {
        return new MFactory(new MapsForgeView(sc, d, skey), d);
    }

    public static MFactory OSM(ServiceContext sc, AbsDispatcher d, String skey) {
        return new MFactory(new NewOsmInteractiveView(sc, d, skey), d);
    }


    public MFactory(MapViewInterface map, final AbsDispatcher dispatcher) {
        d = dispatcher;
        m = map;
        mc = m.getMContext();
    }


    public MapViewInterface base(int size) {
        m.add(new CurrentLocationLayer(mc, d));
        m.add(new NavigationBar(mc, d, size));

        return m;
    }


    public MapViewInterface split() {
        return split(6);
    }

    private MapViewInterface split(int size) {
        base(size);
        m.add(new GpxOverlayListLayer(mc,d));
        m.add(new GpxDynLayer(mc, d, InfoID.TRACKER));

        return m;
    }

    public MapViewInterface tracker(EditorHelper e) {
        return tracker(e, InfoID.EDITOR_DRAFT);
    }

    private MapViewInterface tracker(EditorHelper e, int iid) {
        split(4);

        m.add(new GridDynLayer(mc));
        m.add(new InformationBar(mc, d));
        m.add(new EditorLayer(mc, d, iid, e));

        return m;
    }


    public MapViewInterface map(EditorHelper e, ControlBar b) {
        tracker(e);

        m.add(new CustomBarLayer(mc, b));

        return m;
    }


    public MapViewInterface list(AbsGpxListActivity a) {
        base(4);

        m.add(new GpxOverlayListLayer(mc, d));
        m.add(new GpxDynLayer(mc, d, InfoID.LIST_SUMMARY));
        m.add(new GridDynLayer(mc));
        m.add(new InformationBar(mc, d));
        return m;
    }


    public MapViewInterface editor(EditorHelper e) {
        return tracker(e, InfoID.EDITOR_OVERLAY);
    }


    public MapViewInterface content(EditorHelper e) {
        tracker(e);

        m.add(new GpxDynLayer(mc, d, InfoID.FILEVIEW));

        return m;
    }

    public MapViewInterface node() {
        base(4);

        m.add(new GpxDynLayer(mc, d, InfoID.TRACKER));
        m.add(new GpxDynLayer(mc, d, InfoID.FILEVIEW));
        m.add(new GridDynLayer(mc));

        return m;
    }

}
