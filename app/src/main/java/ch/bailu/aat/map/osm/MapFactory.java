package ch.bailu.aat.map.osm;

import ch.bailu.aat.activities.AbsDispatcher;
import ch.bailu.aat.activities.AbsGpxListActivity;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.map.osm.overlay.CurrentLocationOverlay;
import ch.bailu.aat.map.osm.overlay.control.CustomBarOverlay;
import ch.bailu.aat.map.osm.overlay.control.EditorOverlay;
import ch.bailu.aat.map.osm.overlay.control.InformationBarOverlay;
import ch.bailu.aat.map.osm.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.map.osm.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.map.osm.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.map.osm.overlay.grid.GridDynOverlay;

public class MapFactory {
    private final OsmInteractiveView m;
    private final ServiceContext sc;
    private final DispatcherInterface d;


    public MapFactory(final AbsDispatcher acontext, final String key) {
        sc = acontext.getServiceContext();
        d = acontext;
        m = new OsmInteractiveView(sc, d, key);
    }

    public OsmInteractiveView base(int size) {
        m.add(new CurrentLocationOverlay(m, d));
        m.add(new NavigationBarOverlay(m, d, size));

        return m;
    }


    public OsmInteractiveView split() {
        return split(6);
    }

    private OsmInteractiveView split(int size) {
        base(size);
        m.add(new GpxOverlayListOverlay(m,d, sc));
        m.add(new GpxDynOverlay(m, sc, d, InfoID.TRACKER));

        return m;
    }

    public OsmInteractiveView tracker(EditorHelper e) {
        return tracker(e, InfoID.EDITOR_DRAFT);
    }

    private OsmInteractiveView tracker(EditorHelper e, int iid) {
        split(4);

        m.add(new GridDynOverlay(m, sc));
        m.add(new InformationBarOverlay(m, d));
        m.add(new EditorOverlay(m, sc, d, iid, e));

        return m;
    }


    public OsmInteractiveView map(EditorHelper e, ControlBar b) {
        tracker(e);

        m.add(new CustomBarOverlay(m, b));

        return m;
    }


    public OsmInteractiveView list(AbsGpxListActivity a) {
        base(4);

        m.add(new GpxOverlayListOverlay(m, d, sc));
        m.add(new GpxDynOverlay(m, sc, d, InfoID.LIST_SUMMARY));
        m.add(new GridDynOverlay(m, sc));
        m.add(new InformationBarOverlay(m, d));
        return m;
    }


    public OsmInteractiveView editor(EditorHelper e) {
        return tracker(e, InfoID.EDITOR_OVERLAY);
    }


    public OsmInteractiveView content(EditorHelper e) {
        tracker(e);

        m.add(new GpxDynOverlay(m, sc, d, InfoID.FILEVIEW));

        return m;
    }

    public OsmInteractiveView node() {
        base(4);

        m.add(new GpxDynOverlay(m, sc, d, InfoID.TRACKER));
        m.add(new GpxDynOverlay(m, sc, d, InfoID.FILEVIEW));
        m.add(new GridDynOverlay(m, sc));

        return m;
    }

}
