package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import ch.bailu.aat.preferences.SolidInteger;

public class SolidIntegerView extends SolidView {
    private final SolidInteger sinteger;

    public SolidIntegerView(Context context, SolidInteger i) {
        super(context, i);

        sinteger=i;
    }

        @Override
        public void onRequestNewValue() {
            final EditText input = new EditText(getContext());

            input.setHint(sinteger.getValueAsString());

            new AlertDialog.Builder(getContext())
                    .setTitle(sinteger.getLabel())
//                    .setMessage("blahblah!")
                    .setView(input)
                    .setPositiveButton("OK*", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            sinteger.setValueFromString(input.getText().toString());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();
        }

}
