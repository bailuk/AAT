package ch.bailu.aat.services;

import android.app.Notification;

import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public interface ServiceContext extends ContextWrapperInterface, ServicesInterface {

    void lock(String s);
    void free(String s);

    boolean lock();
    void    free();

    TileRemoverService getTileRemoverService();

    void startForeground(int id, Notification notification);
    void stopForeground(boolean b);

    void appendStatusText(StringBuilder content);
}
