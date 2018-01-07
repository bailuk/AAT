package ch.bailu.aat.views;


import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import ch.bailu.aat.R;

public class EditTextTool extends LinearLayout implements View.OnClickListener {
    public final EditText edit;
    private final ImageButtonView clearText;


    public EditTextTool(EditText e) {
        super(e.getContext());
        edit = e;
        clearText = new ImageButtonView(e.getContext(), R.drawable.edit_clear_all_inverse);
        clearText.setOnClickListener(this);

        setOrientation(HORIZONTAL);

        addView(edit, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,100f));
        add(clearText);

    }



    @Override
    public void onClick(View view) {
        if (view == clearText) edit.setText("");
    }

    public void add(View view) {
        addView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1f));

    }
}
