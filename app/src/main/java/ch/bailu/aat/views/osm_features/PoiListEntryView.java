package ch.bailu.aat.views.osm_features;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.filter_list.PoiListEntry;
import ch.bailu.aat.util.ui.AppTheme;

public class PoiListEntryView extends LinearLayout  {

    private PoiListEntry entry;

    private final OnSelected onSelected;

    private final CheckBox checkBox;
    private final TextView text;


    public PoiListEntryView(final ServiceContext scontext, OnSelected s) {
        super(scontext.getContext());



        onSelected = s;

        checkBox = new CheckBox(getContext());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        checkBox.setClickable(false);
        addView(checkBox);

        text = new TextView(getContext());
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelected.onSelected(entry, 0, null);
            }
        });

        addView(text);


    }


    public void set(final PoiListEntry e) {
        entry = e;

        if (e.isSummary()) {
            checkBox.setVisibility(INVISIBLE);
            AppTheme.main.header(text);
        } else {
            checkBox.setChecked(e.isSelected());
            checkBox.setVisibility(VISIBLE);
            AppTheme.main.content(text);
        }

        text.setText(entry.getTitle());
    }
}