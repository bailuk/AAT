package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import ch.bailu.aat.R;
import ch.bailu.aat.exception.ValidationException;
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
        final AlertDialog.Builder builder = createDefaultDialog(s);

        builder.setPositiveButton(context.getString(R.string.ok), (dialogInterface, i) -> {
        });
        builder.setNegativeButton(context.getString(R.string.cancel), (dialogInterface, i) -> {
        });

        AlertDialog dialog = builder.create();
        final EditText input = new EditText(context);

        input.setInputType(inputType);
        dialog.setView(input);
        input.setText(s.getValueAsString());

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    s.setValueFromString(input.getText().toString());
                    dialog.dismiss();
                } catch (ValidationException e) {
                    input.setError(e.getMessage());
                }
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener( view -> dialog.dismiss());
    }
}

