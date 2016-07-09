package ch.bailu.aat.services.directory;

import java.io.Closeable;
import java.io.File;

import android.app.Activity;
import ch.bailu.aat.activities.AbsServiceLink;
import ch.bailu.aat.helpers.FileAction;
import ch.bailu.aat.services.ServiceContext;

public class DirectoryServiceHelper implements Closeable {

    private final AbsServiceLink slink;
    private final ServiceContext scontext;
    private final File directory;
    private String selection="";

    public DirectoryServiceHelper(AbsServiceLink l, File d) {
        scontext = l.getServiceContext();
        directory = d;
        slink=l;
    }



    public void setSelectionString(String s) {
        selection = s;
    }
    
    
    public void reopen() {
        scontext.getDirectoryService().reopen(directory, selection);
    }

    
    public void requery(String s) {
        selection = s;
        scontext.getDirectoryService().reopen(directory, selection); // or requery ????
    }

    public File getDirectory() {
        return directory;
    }

    public void rescan() {
        scontext.getDirectoryService().rescan();
    }


    public void refreshSelected() {
        new FileAction(slink).reloadPreview();
    }


    public void deleteSelected(Activity activity) {
        new FileAction(slink).delete();
    }



    @Override
    public void close() {}



    public void renameSelectedFile() {
        new FileAction(slink).rename();

    }
}
