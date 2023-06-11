package ch.bailu.aat.map.mapsforge;

import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.map.To;
import ch.bailu.aat_lib.map.MapViewInterface;

public class MapViewLinker implements Observer {
    private final IMapViewPosition master;
    private final IMapViewPosition slave;

    public MapViewLinker(MapViewInterface m, MapViewInterface s) {
        To.view(s).setClickable(false);

        master = m.getMapViewPosition();
        slave = s.getMapViewPosition();
        master.addObserver(this);
    }

    @Override
    public void onChange() {
        setCenter();
        setZoom();
    }

    private void setZoom() {
        int zoom =  master.getZoomLevel()+2;

        if (zoom > slave.getZoomLevelMax()) {
            zoom = master.getZoomLevel()-4;
        }

        zoom = Math.min(slave.getZoomLevelMax(), zoom);
        zoom = Math.max(slave.getZoomLevelMin(), zoom);

        if(slave.getZoomLevel() != zoom)
            slave.setZoomLevel((byte) zoom);
    }

    protected void setCenter() {
        // need to check to avoid circular notifications
        if (!this.master.getCenter().equals(this.slave.getCenter())) {
            this.slave.setCenter(this.master.getCenter());
        }
    }

    public void removeObserver() {
        this.master.removeObserver(this);
    }
}
