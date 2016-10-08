package ch.bailu.aat.views;

import android.view.View;

import ch.bailu.aat.R;
import ch.bailu.aat.views.description.MultiView;


public class MvNextButton extends ImageButtonView {
    public MvNextButton(final MultiView mv) {
        super(mv.getContext(), R.drawable.go_next_inverse);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.setNext();
            }
        });
    }
}
