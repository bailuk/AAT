package ch.bailu.aat.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Locale;

import ch.bailu.aat_lib.search.poi.OsmApiConfiguration;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.preferences.TitleView;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class OsmApiEditorView extends LinearLayout {
    private final EditTextTool editor;
    private final TextView preview;

    private final MultiView inputMultiView;


    public OsmApiEditorView(Context context, OsmApiConfiguration osmApi, UiTheme theme) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        preview = new TextView(getContext());

        VerticalScrollView scroller = new VerticalScrollView(getContext());
        scroller.add(preview);
        editor = new EditTextTool(new TagEditor(getContext(),
                osmApi.getBaseDirectory()), LinearLayout.VERTICAL, theme);

        inputMultiView = new MultiView(getContext(), osmApi.getApiName());
        inputMultiView.add(editor);
        inputMultiView.add(scroller);

        preview.setOnClickListener(view -> inputMultiView.setNext());
        preview.setText(osmApi.getUrlPreview(editor.edit.getText().toString()));

        theme.content(preview);
        addView(createTitle(osmApi, theme));
        addView(inputMultiView);

    }


    private View createTitle(OsmApiConfiguration osmApi, UiTheme theme) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        String[] strings = osmApi.getUrlStart().split(osmApi.getApiName().toLowerCase(Locale.ROOT));

        TextView b = new TitleView(getContext(), osmApi.getApiName(), AppTheme.search);
        b.setSingleLine();

        if (strings.length>1) {
            TextView a = new TextView(getContext());
            TextView c = new TextView(getContext());

            a.setText(strings[0]);
            a.setSingleLine();

            c.setText(strings[1]);
            c.setSingleLine();
            c.setEllipsize(TextUtils.TruncateAt.END);

            theme.content(a);
            theme.content(c);
            b.setTextColor(theme.getHighlightColor());

            layout.addView(a);
            layout.addView(b);
            layout.addView(c);
        } else {
            layout.addView(b);
        }

        layout.setOnClickListener(view -> {
            preview.setText(osmApi.getUrlPreview(editor.edit.getText().toString()));
            inputMultiView.setNext();
        });

        return layout;
    }

    @Override
    @NonNull
    public String toString() {
        return editor.edit.getText().toString();
    }

    public void insertLine(String s) {
        editor.insertLine(s);
        inputMultiView.setActive(0);
    }

    public void setText(String query) {
        editor.edit.setText(query);
        inputMultiView.setActive(0);
    }
}
