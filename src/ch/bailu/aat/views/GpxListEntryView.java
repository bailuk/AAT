package ch.bailu.aat.views;


import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.OsmPreviewGenerator;


public class GpxListEntryView extends  DescriptionViewGroup {
    private static final String SOLID_KEY = GpxListEntryView.class.getSimpleName();   
    private final static int SPACE=20;

    private final PreviewView preview;
    private final ServiceContext scontext;


    
    public GpxListEntryView(ServiceContext sc, ContentDescription[] description) {
        super(sc.getContext(), SOLID_KEY, INFO_ID_ALL);

        scontext=sc;

        preview = new PreviewView(scontext);
        addView(preview);

        init(getContext(),description);
    }


    private void init(Context context,
            ContentDescription[] d) {

        final SubEntryView v[] = new SubEntryView[d.length];

        for (int i=0; i<d.length; i++) {
            v[i] = new SubEntryView(context,solidKey, d[i]);
            if (i==0) v[i].highlight();
            addView(v[i]);
        }
        init(d,v);
    }


    @Override
    protected void onMeasure(int wspec, int hspec) {
        int width,height;

        measureChildren(wspec,hspec);

        width = AppLayout.getDimension(wspec, AppLayout.getScreenSmallSide(getContext()));
        height= AppLayout.getDimension(hspec, OsmPreviewGenerator.BITMAP_SIZE);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int xpos=0; int ypos=0;
        final int width=r-l-OsmPreviewGenerator.BITMAP_SIZE;

        for (int i=0; i<getDescriptionCount(); i++) {
            int w=getDescriptionView(i).getMeasuredWidth();
            int h=getDescriptionView(i).getMeasuredHeight();
            if ((xpos > 0) && ((w+xpos) > width)) {
                ypos+=h; xpos=0;
            }
            getDescriptionView(i).layout(xpos, ypos, xpos+w, ypos+h);
            xpos+=(w+SPACE);
        }

        preview.layout(width, 0, width+OsmPreviewGenerator.BITMAP_SIZE, OsmPreviewGenerator.BITMAP_SIZE);
    }


    private class SubEntryView extends TrackDescriptionView {

        TextView entry;
        ContentDescription data;

        public SubEntryView(Context context, String key, ContentDescription d) {
            super(context, key,INFO_ID_ALL);

            data=d;

            entry = new TextView(context);
            entry.setTextColor(Color.WHITE);
            addView(entry);
        }

        public void highlight() {
            entry.setTypeface(entry.getTypeface(), 1);
            entry.setBackgroundColor(AppTheme.getAltBackgroundColor());
        }


        @Override 
        protected void onMeasure(int w, int h) {
            entry.measure(w, h);
            setMeasuredDimension(entry.getMeasuredWidth(), entry.getMeasuredHeight());
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            entry.layout(0,0,r-l,b-t);
        }

        @Override
        public void updateGpxContent(GpxInformation info) {
            if (filter.pass(info)) {
                data.updateGpxContent(info);
                updateText(data.getValue(), data.getUnit());

                preview.setFilePath(info.getPath());
            }
        }

        private void updateText(String value, String unit) {
            String text;
            if (unit.length()>0) text = value + " " + unit;
            else text=value;
            entry.setText(text);
        }


    }
}
