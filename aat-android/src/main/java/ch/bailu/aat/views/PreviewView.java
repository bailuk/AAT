package ch.bailu.aat.views;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjBitmap;
import ch.bailu.aat.services.directory.AndroidSummaryConfig;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public class PreviewView extends ImageObjectView implements OnContentUpdatedInterface {


    public PreviewView(ServiceContext sc) {
        super(sc, R.drawable.open_menu_light);
        ToolTip.set(this, R.string.tt_menu_file);

    }


    public void setFilePath(String fileID) {
        setFilePath(FocAndroid.factory(getContext(), fileID));
    }


    public void setFilePath(Foc fileID) {
        final Foc file = AndroidSummaryConfig
                .getReadablePreviewFile(getContext(), fileID);
        setPreviewPath(file);
    }



    public void setPreviewPath(Foc file) {
        setImageObject(file.getPath(), new ObjBitmap.Factory());
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
         setFilePath(info.getFile());
    }

}
