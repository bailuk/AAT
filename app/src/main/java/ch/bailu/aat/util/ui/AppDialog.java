package ch.bailu.aat.util.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;

import ch.bailu.aat.R;

public abstract class AppDialog {

    class PositiveClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            onPositiveClick();
        }
    }
    class NegativeClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            onNegativeClick();
        }
    }

    class NeutralClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            onNeutralClick();
        }
    }

    protected void onNegativeClick() {}
    protected abstract void onPositiveClick();
    public void onNeutralClick() {}

    public void displayYesNoDialog(Activity activity, String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Dialog dialog;

        builder.setTitle(title);
        builder.setMessage(text);
        builder.setCancelable(true);
        builder.setPositiveButton(activity.getString(R.string.dialog_yes), new PositiveClickListener());
        builder.setNegativeButton(activity.getString(R.string.dialog_no), new NegativeClickListener());
        dialog = builder.create();
                
        dialog.setOwnerActivity(activity);
        dialog.show();
    }



    public void displayTextDialog(Activity activity, String title, EditText edit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Dialog dialog;

        builder.setTitle(title);
        builder.setView(edit);
        builder.setCancelable(true);
        builder.setPositiveButton(activity.getString(R.string.dialog_ok), 
                new PositiveClickListener());
        builder.setNegativeButton(activity.getString(R.string.dialog_cancel), 
                new NegativeClickListener());

        dialog = builder.create();
        
        dialog.setOwnerActivity(activity);
        dialog.show();
    }

    public void displaySaveDiscardDialog(Activity activity, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Dialog dialog;

        builder.setTitle(title);
        builder.setMessage(activity.getString(R.string.dialog_modified));
        builder.setCancelable(true);
        builder.setPositiveButton(activity.getString(R.string.dialog_save), new PositiveClickListener());
        builder.setNeutralButton(activity.getString(R.string.dialog_discard), new NeutralClickListener());
        builder.setNegativeButton(activity.getString(R.string.dialog_cancel), new NegativeClickListener());

        dialog = builder.create();
        
        dialog.setOwnerActivity(activity);
        dialog.show();

    }
}
