package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;

import ch.bailu.aat_lib.preferences.AbsSolidType;

public abstract class AbsSolidDialog  {

    public static AlertDialog.Builder createDefaultDialog(Context context, AbsSolidType s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle(s.getLabel());
        if (s.getIconResource() != 0) {
            dialog.setIcon(s.getIconResource());
        }

        return dialog;
    }


    public static AlertDialog.Builder createDefaultDialog(Context c, String s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(s);

        return dialog;
    }

}
