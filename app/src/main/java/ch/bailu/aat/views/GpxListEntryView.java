package ch.bailu.aat.views;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.R;
import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.util_java.foc.Foc;

public class GpxListEntryView extends LinearLayout implements OnContentUpdatedInterface {

    private final PreviewView preview;
    private final TextView title, text;

    private final ContentDescription[] descriptions;

    private Foc file = FocAndroid.NULL;

    public GpxListEntryView(final AbsServiceLink acontext, ContentDescription[] d) {
        super(acontext);

        descriptions = d;

        setOrientation(HORIZONTAL);

        AbsListView.LayoutParams p = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(p);

        final LinearLayout textLayout;
        textLayout = new LinearLayout(acontext);
        textLayout.setOrientation(VERTICAL);
        addViewWeight(textLayout);

        int previewSize = AppLayout.getBigButtonSize(acontext);
        preview = new PreviewView(acontext.getServiceContext());
        addView(preview, previewSize, previewSize);

        preview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new FileMenu(acontext, file).showAsDialog(acontext);
            }
        });


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
    public void onContentUpdated(int iid, GpxInformation info) {
        file = info.getFile();

        for (ContentDescription description : descriptions) {
            description.onContentUpdated(iid, info);
        }
        updateText();

        preview.onContentUpdated(iid, info);
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
