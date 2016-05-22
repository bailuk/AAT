package ch.bailu.aat.views;


import java.io.File;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.ImageObject;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.views.map.OsmPreviewGenerator;


public class GpxListEntryView extends  DescriptionViewGroup {
    private static final String SOLID_KEY = GpxListEntryView.class.getSimpleName();   
    private final static int SPACE=20;

    private final ImageView imageView;
    private final CacheService.Self loader;

    private ImageObject imageHandle=ImageObject.NULL;

    private boolean isAttached=false;
    private String imageToLoad=null;


    public GpxListEntryView(Context context, ContentDescription[] description, CacheService.Self l) {
        super(context, SOLID_KEY, INFO_ID_ALL);

        loader = l;

        imageView = new ImageView(context);
        addView(imageView);

        init(context,description);
    }


    private void init(Context context,
            ContentDescription[] d) {

        SubEntryView v[] = new SubEntryView[d.length];

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

        imageView.layout(width, 0, width+OsmPreviewGenerator.BITMAP_SIZE, OsmPreviewGenerator.BITMAP_SIZE);
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

                imageToLoad = info.getPath();
                loadAndDisplayImage();
            }
        }

        private void updateText(String value, String unit) {
            String text;
            if (unit.length()>0) text = value + " " + unit;
            else text=value;
            entry.setText(text);
        }

       
    }




    private void loadAndDisplayImage() {
        if (imageToLoad != null && isAttached) {

            freeImageHandle();
            try {
                final File file = AppDirectory.getPreviewFile(new File(imageToLoad));
                final ObjectHandle  h=loader.getObject(file.getAbsolutePath(), new ImageObject.Factory());

                if (ImageObject.class.isInstance(h)) {
                    imageHandle = (ImageObject) h;
                    displayImage();
                }

            } catch (IOException e) {
                AppLog.e(getContext(), e);
            }
            imageToLoad=null;
        }
    }


    private void freeImageHandle() {
        imageHandle.free();
        imageHandle = ImageObject.NULL;
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached=true;

        AppBroadcaster.register(getContext(), 
                onFileChanged, 
                AppBroadcaster.FILE_CHANGED_INCACHE);
        loadAndDisplayImage();
    }


    @Override
    public void onDetachedFromWindow() {
        getContext().unregisterReceiver(onFileChanged);
        freeImageHandle();

        isAttached=false;
        super.onDetachedFromWindow();
    }


    private void displayImage() {
        imageView.setImageDrawable(imageHandle.getDrawable());

    }


    private BroadcastReceiver onFileChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String file = imageHandle.toString();

            if (AppBroadcaster.hasFile(intent, file)) displayImage();
        }
    };

}
