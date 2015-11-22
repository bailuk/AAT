package ch.bailu.aat.dispatcher;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.directory.DirectoryService;

public class CurrentFileSource extends ContentSource {

    private final DirectoryService directoryService;


    private BroadcastReceiver onFileProcessed = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            File file = new File(directoryService.getCurrent().getPath());

            if (AppBroadcaster.hasFile(intent, file.getAbsolutePath())) {
                forceUpdate();
            }
        }

    };

    public CurrentFileSource (DirectoryService d) {
        directoryService = d;
        AppBroadcaster.register(directoryService, onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
    }


    @Override
    public void cleanUp() {
        directoryService.unregisterReceiver(onFileProcessed);
    }

    @Override
    public void forceUpdate() {
        updateGpxContent(directoryService.getCurrent());
    }
}
