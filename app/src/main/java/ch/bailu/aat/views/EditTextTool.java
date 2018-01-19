package ch.bailu.aat.views;


import android.text.Layout;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.views.bar.ControlBar;

public class EditTextTool extends LinearLayout implements View.OnClickListener {
    public final EditText edit;
    public final ControlBar bar;

    private final ImageButton clearText;



    public EditTextTool(EditText e, int orientation) {
        super(e.getContext());
        edit = e;
        bar = new ControlBar(getContext(),orientation,6);

        clearText = bar.addImageButton(R.drawable.edit_clear_all_inverse);
        clearText.setOnClickListener(this);

        setOrientation(HORIZONTAL);

        addView(edit, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,100f));
        add(bar);

    }



    @Override
    public void onClick(View view) {
        if (view == clearText) {
            deleteCurrentLine();
        }
    }

    public void add(View view) {
        addView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

    }

    public void deleteCurrentLine() {
        int selectionStart = Selection.getSelectionStart(edit.getText());

        if (!(selectionStart == -1)) {
            Layout layout = edit.getLayout();

            int line = layout.getLineForOffset(selectionStart);
            int start = layout.getLineStart(line);
            int end = layout.getLineEnd(line);

            if (start > 0) start --;
            edit.getText().delete(start, end);
        }
    }


    public void insertLine(String l) {
        int selectionStart = Selection.getSelectionStart(edit.getText());

        if (!(selectionStart == -1)) {
            Layout layout = edit.getLayout();

            int line = layout.getLineForOffset(selectionStart);
            int start = layout.getLineStart(line);
            int end = layout.getLineEnd(line);

            if (start > 0) start --;
            edit.getText().insert(end, l + "\n");
        }
    }

}
