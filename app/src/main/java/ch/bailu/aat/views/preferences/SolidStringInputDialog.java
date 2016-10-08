package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.AbsSolidType;

public class SolidStringInputDialog extends AbsSolidDialog {

    public SolidStringInputDialog(final AbsSolidType s) {

        final Context context = s.getContext();

        final AlertDialog.Builder dialog = createDefaultDialog(s);
        final EditText input = new EditText(context);

        input.setHint(s.getValueAsString());
        input.setText(s.getValueAsString());

        dialog.setView(input);

        dialog.setPositiveButton(context.getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                s.setValueFromString(input.getText().toString());
            }
        });
        dialog.show();
    }
}
