package ch.bailu.aat.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public class GpxListEntryView extends LinearLayout implements OnContentUpdatedInterface {

    private final PreviewView preview;
    private final LabelTextView labelTextView;

    private final ContentDescription[] descriptions;

    private Foc file = FocAndroid.NULL;

    public GpxListEntryView(final AbsServiceLink acontext, ContentDescription[] d, UiTheme theme) {
        super(acontext);

        descriptions = d;

        setOrientation(HORIZONTAL);

        AbsListView.LayoutParams p = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(p);

        labelTextView = new LabelTextView(getContext(), "", theme);
        addViewWeight(labelTextView);

        int previewSize = AppLayout.getBigButtonSize(acontext);
        preview = new PreviewView(acontext.getServiceContext());
        addView(preview, previewSize, previewSize);

        preview.setOnClickListener(view -> new FileMenu(acontext, file).showAsDialog(acontext));
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


    public void themify(UiTheme theme) {
        labelTextView.themify(theme);
        theme.button(labelTextView);
    }

    private final StringBuilder builder = new StringBuilder(20);
    private void updateText() {

        builder.setLength(0);
        for (int i=0; i< descriptions.length; i++) {
            if (i==0) {
                labelTextView.setLabel(descriptions[i].getValueAsString());
            } else {
                if (i > 1) builder.append(" - ");
                builder.append(descriptions[i].getValueAsString());
            }
        }

        labelTextView.setText(builder);
    }
}
