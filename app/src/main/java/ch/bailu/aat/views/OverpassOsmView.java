package ch.bailu.aat.views;




/*
public class OverpassOsmView extends OsmInteractiveView implements OnClickListener {

    private final View save, up, down, delete;
    
    private OverpassList  overpassList;

    
    public OverpassOsmView(Context context, String key) {
        super(context, key);

        
        
        try {
            overpassList = new OverpassFileList(context);
        } catch (IOException e) {
            overpassList = OverpassList.NULL_LIST;
            AppLog.e(context, e);
        }
        
        
        ControlBar bar = new ControlBar(
                context, 
                AppLayout.getOrientationAlongSmallSide(context));
        
        
        save = bar.addImageButton(R.drawable.document_save);
        up = bar.addImageButton(R.drawable.go_up);
        down = bar.addImageButton(R.drawable.go_down);
        delete = bar.addImageButton(R.drawable.edit_delete);
        
        bar.setOnClickListener1(this);

        
        OsmOverlay overlayList[] = {
                new GpxDynOverlay(this, GpxInformation.ID.INFO_ID_TRACKER), 
                new GpxDynAutoFrameOverlay(this, 
                        GpxInformation.ID.INFO_ID_OVERLAY, 
                        AppTheme.OVERLAY_COLOR[0]),
                new GridDynOverlay(this),
                new CurrentLocationOverlay(this),
                new CustomBarOverlay(this, bar),
                new NavigationBarOverlay(this),
                new InformationBarOverlay(this)
        };

        setOverlayList(overlayList);

    }

    
    @Override
    public void onClick(View v) {
        if (v==up) {
            overpassList.next();
            
        } else if (v==down) {
            overpassList.previous();
            
        } else if (v==save) {
            try {
                overpassList.save();
            } catch (IOException e) {
                overpassList=OverpassList.NULL_LIST;
            }
            
        } else if (v==delete) {
            overpassList.delete();
            
        }
    }    
}
*/