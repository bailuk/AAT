package ch.bailu.aat.map;

import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.EditorSourceInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer;
import ch.bailu.aat.map.layer.control.CustomBarLayer;
import ch.bailu.aat.map.layer.control.EditorLayer;
import ch.bailu.aat.map.layer.control.InformationBarLayer;
import ch.bailu.aat.map.layer.control.NavigationBarLayer;
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat_lib.map.layer.gpx.GpxOverlayListLayer;
import ch.bailu.aat_lib.map.layer.grid.Crosshair;
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer;
import ch.bailu.aat.map.mapsforge.MapsForgeView;
import ch.bailu.aat.map.mapsforge.MapsForgeViewBase;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class MapFactory {

    private final MapsForgeViewBase m;
    private final DispatcherInterface d;
    private final MapContext mc;
    private final StorageInterface s;
    private final ServicesInterface ser;


    public static MapFactory DEF(AbsDispatcher d, String skey) {
        return MF(d.getServiceContext(), d, skey);
    }

    public static MapFactory MF(ServiceContext sc, AbsDispatcher d, String skey) {
        return new MapFactory(new MapsForgeView(sc, d, skey), d);
    }


    public MapFactory(MapsForgeViewBase map, final AbsDispatcher dispatcher) {
        d = dispatcher;
        m = map;
        mc = m.getMContext();
        s = new Storage(map.getContext());
        ser = To.scontext(map.getMContext());
        dispatcher.addLC(m);
    }


    public MapsForgeViewBase base(int size) {
        m.add(new CurrentLocationLayer(mc, d));
        m.add(new NavigationBarLayer(mc, d, size));
        return m;
    }


    public MapsForgeViewBase split() {
        m.add(new CurrentLocationLayer(mc, d));
        m.add(new GpxOverlayListLayer(s,mc,ser, d));
        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.EDITOR_DRAFT));
        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.TRACKER));
        m.add(new Crosshair());

        return m;
    }

    public MapsForgeViewBase tracker(EditorSourceInterface e) {
        return tracker(e, InfoID.EDITOR_DRAFT);
    }

    private MapsForgeViewBase tracker(EditorSourceInterface e, int iid) {
        base(4);
        m.add(new GpxOverlayListLayer(s,mc,ser, d));
        m.add(new EditorLayer(ser, s, mc, d, iid, e));
        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.FILEVIEW));
        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.TRACKER));
        m.add(new GridDynLayer(ser, s,mc));
        m.add(new InformationBarLayer(mc, d));

        return m;
    }


    public MapsForgeViewBase map(EditorSourceInterface e, ControlBar b) {
        tracker(e);

        m.add(new CustomBarLayer(mc, b, AppTheme.bar));

        return m;
    }


    public MapsForgeViewBase list(AbsGpxListActivity a) {
        base(4);

        m.add(new GpxOverlayListLayer(s,mc, ser, d));
        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.LIST_SUMMARY));
        m.add(new GridDynLayer(ser, s,mc));
        m.add(new InformationBarLayer(mc, d));
        return m;
    }


    public MapsForgeViewBase editor(EditorSourceInterface e) {
        return tracker(e, InfoID.EDITOR_OVERLAY);
    }


    public MapsForgeViewBase content(EditorSourceInterface e) {
        return editor(e);
    }


    public MapsForgeViewBase node() {
        base(4);

        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.TRACKER));
        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.FILEVIEW));
        m.add(new GridDynLayer(ser, s,mc));

        return m;
    }


    public MapsForgeViewBase externalContent() {
        m.add(new GpxOverlayListLayer(s,mc, ser, d));
        m.add(new GpxDynLayer(s,mc, ser, d, InfoID.FILEVIEW));
        m.add(new CurrentLocationLayer(mc, d));
        m.add(new GridDynLayer(ser, s,mc));
        m.add(new NavigationBarLayer(mc, d));
        m.add(new InformationBarLayer(mc,d));

        return m;
    }
}
