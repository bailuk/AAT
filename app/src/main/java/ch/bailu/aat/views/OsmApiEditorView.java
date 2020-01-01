package ch.bailu.aat.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.bailu.aat.util.OsmApiConfiguration;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.preferences.TitleView;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class OsmApiEditorView extends LinearLayout {
    private final EditTextTool editor;
    private final TextView preview;

    private final MultiView inputMultiView;


    public OsmApiEditorView(Context context, OsmApiConfiguration osmApi) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);



        preview = new TextView(getContext());

        VerticalScrollView scroller = new VerticalScrollView(getContext());
        scroller.add(preview);
        editor = new EditTextTool(new TagEditor(getContext(),
                osmApi.getBaseDirectory()), LinearLayout.VERTICAL);

        inputMultiView = new MultiView(getContext(), osmApi.getApiName());
        inputMultiView.add(editor);
        inputMultiView.add(scroller);

        preview.setOnClickListener(view -> inputMultiView.setNext());
        preview.setText(osmApi.getUrlPreview(editor.edit.getText().toString()));

        addView(createTitle(osmApi));
        addView(inputMultiView);

    }



    private View createTitle(OsmApiConfiguration osmApi) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        String[] strings = osmApi.getUrlStart().split(osmApi.getApiName().toLowerCase());

        TextView b = new TitleView(getContext(), osmApi.getApiName());
        b.setSingleLine();

        if (strings.length>1) {
            TextView a = new TextView(getContext());
            TextView c = new TextView(getContext());

            a.setText(strings[0]);
            a.setSingleLine();

            c.setText(strings[1]);
            c.setSingleLine();
            c.setEllipsize(TextUtils.TruncateAt.END);

            a.setTextColor(Color.WHITE);
            c.setTextColor(Color.WHITE);

            layout.addView(a);
            layout.addView(b);
            layout.addView(c);
        } else {
            layout.addView(b);
        }

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                preview.setText(osmApi.getUrlPreview(editor.edit.getText().toString()));
                inputMultiView.setNext();
            }
        });

        return layout;
    }

    @Override
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
