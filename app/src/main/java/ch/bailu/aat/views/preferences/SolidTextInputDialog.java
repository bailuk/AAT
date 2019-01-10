package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.AbsSolidType;
import ch.bailu.aat.preferences.SolidInteger;
import ch.bailu.aat.preferences.SolidLong;
import ch.bailu.aat.preferences.SolidString;

public class SolidTextInputDialog extends AbsSolidDialog {


    public static int INTEGER = InputType.TYPE_CLASS_NUMBER;
    public static int INTEGER_SIGNED = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
    public static int FLOAT = InputType.TYPE_CLASS_NUMBER   | InputType.TYPE_NUMBER_FLAG_DECIMAL;

    public static int TEXT = InputType.TYPE_CLASS_TEXT;


    public SolidTextInputDialog(final AbsSolidType s, int inputType) {
        final Context context = s.getContext();

        final AlertDialog.Builder dialog = createDefaultDialog(s);
        final EditText input = new EditText(context);

        input.setText(s.getValueAsString());
        input.setInputType(inputType);

        dialog.setView(input);

        dialog.setPositiveButton(context.getString(R.string.dialog_ok),
                (dialog1, whichButton) -> s.setValueFromString(input.getText().toString()));

        dialog.show();

    }
}
