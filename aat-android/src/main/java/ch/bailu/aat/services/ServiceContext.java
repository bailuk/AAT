package ch.bailu.aat.services;

import android.app.Notification;

import ch.bailu.aat.services.elevation.ElevationService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.aat_lib.service.IconMapServiceInterface;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;

public interface ServiceContext extends ContextWrapperInterface, ServicesInterface {

    void lock(String s);
    void free(String s);

    boolean lock();
    void    free();

    ElevationService getElevationService();
    IconMapServiceInterface getIconMapService();
    TileRemoverService getTileRemoverService();
    SensorServiceInterface getSensorService();

    void startForeground(int id, Notification notification);
    void stopForeground(boolean b);

    void appendStatusText(StringBuilder content);

}
