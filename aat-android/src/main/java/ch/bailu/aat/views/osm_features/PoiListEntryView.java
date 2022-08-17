package ch.bailu.aat.views.osm_features;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.search.poi.PoiListEntry;

public class PoiListEntryView extends LinearLayout  {

    private PoiListEntry entry;

    private final CheckBox checkBox;
    private final TextView textView;


    public PoiListEntryView(final Context context, OnSelected onSelected, UiTheme theme) {
        super(context);

        setOnClickListener(v -> onSelected.onSelected(entry, 0, null));

        checkBox = new CheckBox(getContext());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {});
        checkBox.setClickable(false);
        textView = new TextView(getContext());

        AppTheme.padding(textView, 10);
        theme.content(checkBox);
        theme.header(textView);
        theme.button(textView);

        addView(checkBox);
        addView(textView);
    }


    public void set(final PoiListEntry e) {
        entry = e;

        if (e.isSummary()) {
            checkBox.setVisibility(GONE);
            checkBox.setText("");

            textView.setText(entry.getTitle());
            textView.setVisibility(VISIBLE);

        } else {
            textView.setVisibility(GONE);
            textView.setText("");

            checkBox.setChecked(e.isSelected());
            checkBox.setText(entry.getTitle());
            checkBox.setVisibility(VISIBLE);
        }
    }
}
