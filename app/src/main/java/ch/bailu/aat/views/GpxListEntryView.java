package ch.bailu.aat.views;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceContext;

public class GpxListEntryView extends LinearLayout implements OnContentUpdatedInterface {

    private final int previewSize;
    private final PreviewView preview;
    private final TextView title, text;

    private final ContentDescription[] descriptions;


    public GpxListEntryView(ServiceContext sc, ContentDescription[] d) {
        super(sc.getContext());

        descriptions = d;

        setOrientation(HORIZONTAL);

        AbsListView.LayoutParams p = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(p);

        final LinearLayout textLayout;
        textLayout = new LinearLayout(sc.getContext());
        textLayout.setOrientation(VERTICAL);
        addViewWeight(textLayout);

        previewSize = AppTheme.getBigButtonSize(sc.getContext());
        preview = new PreviewView(sc);
        addView(preview, previewSize, previewSize);

        title = new TextView(getContext());
        title.setTextColor(Color.WHITE);
        textLayout.addView(title);

        text = new TextView(getContext());
        text.setTextColor(Color.LTGRAY);
        textLayout.addView(text);

    }


    private void addViewWeight(View v) {
        addView(v);

        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) v.getLayoutParams();
        l.weight = 1;
        v.setLayoutParams(l);
    }

    @Override
    public void onContentUpdated(GpxInformation info) {


        for (ContentDescription description : descriptions) {
            description.onContentUpdated(info);
        }
        updateText();
        preview.setFilePath(info.getPath());
    }


    private final StringBuilder builder = new StringBuilder(20);
    private void updateText() {

        builder.setLength(0);
        for (int i=0; i< descriptions.length; i++) {
            if (i==0) {
                title.setText(descriptions[i].getValueAsString());
            } else {
                if (i > 1) builder.append(" - ");
                builder.append(descriptions[i].getValueAsString());
            }
        }

        text.setText(builder);
    }
}
