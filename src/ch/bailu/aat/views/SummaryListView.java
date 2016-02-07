package ch.bailu.aat.views;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SummaryListView extends TrackDescriptionView {
    private final ListView list;
    
    private final TextView[] label;
    private final ContentDescription[] data;

    public SummaryListView(Context context, String key, int f, ContentDescription[] d) {
        super(context, key,f);

        data=d;

        label = new TextView[data.length];
        for (int i=0; i<data.length; i++) {
            label[i] = new MyTextView(context);
        }

        list = new ListView(context);
        list.setAdapter(new MyListAdapter());

        addView(list);
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            for (int i=0; i<data.length; i++) {
                data[i].updateGpxContent(info);
                updateLabel(i);
            }
        }   
    }

    private void updateLabel(int i) {
        String text=String.format("%s:  %s%s", 
                data[i].getLabel(), 
                data[i].getValue(), 
                data[i].getUnit());
        label[i].setText(text);
    }


    @Override
    protected void onLayout(boolean c, int l, int t, int r, int b) {
        if (c) list.layout(0, 0, r-l, b-t);
    }


    private int parentWidthHack=2000;

    @Override
    protected void onMeasure(int w, int h) {
        parentWidthHack=MeasureSpec.getSize(w);
        super.onMeasure(w, h);
    }

    private class MyTextView extends TextView {

        public MyTextView(Context context) {
            super(context);
            this.setTextColor(Color.WHITE);
        }

        @Override
        public void onMeasure(int w, int h) {
            w = MeasureSpec.makeMeasureSpec(parentWidthHack, MeasureSpec.EXACTLY);
            super.onMeasure(w, h);
        }
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {return label.length;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return label[position];
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
