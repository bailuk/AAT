package ch.bailu.aat.views;


import android.text.Layout;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.bar.ControlBar;

public class EditTextTool extends LinearLayout implements View.OnClickListener {
    public final EditText edit;
    public final ControlBar bar;

    private final ImageButtonViewGroup clearText;



    public EditTextTool(EditText e, int orientation, UiTheme theme) {
        super(e.getContext());
        edit = e;
        bar = new ControlBar(getContext(),orientation,6, theme);

        clearText = bar.addImageButton(R.drawable.edit_clear_all_inverse);
        clearText.setOnClickListener(this);
        ToolTip.set(clearText, R.string.tt_nominatim_clear);

        setOrientation(HORIZONTAL);

        addView(edit, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,100f));
        add(bar);

    }



    @Override
    public void onClick(View view) {
        if (view == clearText) {
            new CurrentLine().delete();
        }
    }

    public void add(View view) {
        addView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

    }

    public void insertLine(String s) {
        new CurrentLine().insert(s);
    }


    public class CurrentLine {

        final int start, end;


        public CurrentLine() {
            int selectionStart = Selection.getSelectionStart(edit.getText());

            if (!(selectionStart == -1)) {
                Layout layout = edit.getLayout();

                int line = layout.getLineForOffset(selectionStart);
                start = layout.getLineStart(line);
                end = layout.getLineEnd(line);

            } else {
                start = end = -1;
            }
        }


        public void delete() {
            if (start > -1)
                edit.getText().delete(start, end);
        }


        public void insert(String l) {
            if (start > -1) {
                edit.getText().insert(end, l + "\n");
            }
        }
    }
}
