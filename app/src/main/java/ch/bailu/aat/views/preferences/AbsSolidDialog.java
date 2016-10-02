package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;

import ch.bailu.aat.preferences.SolidType;

public abstract class AbsSolidDialog {

    public static AlertDialog.Builder createDefaultDialog(SolidType s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(s.getContext());

        dialog.setTitle(s.getLabel());
        if (s.getIconResource() != 0) {
            dialog.setIcon(s.getIconResource());
        }

        return dialog;
    }

}
