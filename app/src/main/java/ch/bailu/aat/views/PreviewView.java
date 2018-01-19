package ch.bailu.aat.views;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.directory.SummaryConfig;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

public class PreviewView extends ImageObjectView implements OnContentUpdatedInterface {


    public PreviewView(ServiceContext sc) {
        super(sc, R.drawable.open_menu_light);
    }


    public void setFilePath(String fileID) {
        setFilePath(FocAndroid.factory(getContext(), fileID));
    }


    public void setFilePath(Foc fileID) {
        final Foc file = SummaryConfig
                .getReadablePreviewFile(getContext(), fileID);
        setPreviewPath(file);
    }



    public void setPreviewPath(Foc file) {
        setImageObject(file.getPath(), new ImageObject.Factory());
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setFilePath(info.getFile());
    }

}
