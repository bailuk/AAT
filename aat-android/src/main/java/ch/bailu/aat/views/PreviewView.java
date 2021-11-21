package ch.bailu.aat.views;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjBitmap;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.service.directory.SummaryConfig;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public class PreviewView extends ImageObjectView implements OnContentUpdatedInterface {

    private final SummaryConfig summaryConfig;

    public PreviewView(ServiceContext sc, SummaryConfig summaryConfig) {
        super(sc, R.drawable.open_menu_light);
        this.summaryConfig = summaryConfig;
        ToolTip.set(this, R.string.tt_menu_file);

    }


    public void setFilePath(String fileID) {
        setFilePath(FocAndroid.factory(getContext(), fileID));
    }


    public void setFilePath(Foc fileID) {
        final Foc file = summaryConfig.getPreviewFile(fileID);
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
