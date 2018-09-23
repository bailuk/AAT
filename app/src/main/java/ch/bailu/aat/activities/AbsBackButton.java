package ch.bailu.aat.activities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class AbsBackButton extends AbsActivity {

    @Override
    public void onBackPressed() {
        View view = getWindow().getDecorView();

        if (onBackPressed(view) == false)
            super.onBackPressed();

    }

    public void onBackPressedMenuBar() {
        super.onBackPressed();
    }

    public interface OnBackPressed {
        boolean onBackPressed();
    }

    public abstract static class OnBackPressedListener extends View implements OnBackPressed {
        public OnBackPressedListener(Context context) {
            super(context);
            setVisibility(INVISIBLE);
        }

        @Override
        public abstract boolean onBackPressed();
    }

    private boolean onBackPressed(View view) {
        if (view instanceof OnBackPressed) {
            if (((OnBackPressed) view).onBackPressed())
                return true;
        }

        if (view instanceof ViewGroup) {
            if (onBackPressedChildren((ViewGroup) view))
                return true;
        }
        return false;
    }

    private boolean onBackPressedChildren(ViewGroup parent) {
        int count = parent.getChildCount();

        for (int i=0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (onBackPressed(view)) return true;
        }
        return false;
    }
}
