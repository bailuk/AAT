package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import ch.bailu.aat_lib.exception.ValidationException;
import ch.bailu.aat_lib.preferences.AbsSolidType;
import ch.bailu.aat_lib.resources.Res;

public class SolidTextInputDialog extends AbsSolidDialog {


    public static final int INTEGER = InputType.TYPE_CLASS_NUMBER;
    public static final int INTEGER_SIGNED = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
    public static final int FLOAT = InputType.TYPE_CLASS_NUMBER   | InputType.TYPE_NUMBER_FLAG_DECIMAL;

    public static final int TEXT = InputType.TYPE_CLASS_TEXT;


    public SolidTextInputDialog(Context context, final AbsSolidType s, int inputType,
                                final View.OnClickListener onClick) {

        final AlertDialog.Builder builder = createDefaultDialog(context,s);

        builder.setPositiveButton(Res.str().ok(), (dialogInterface, i) -> {
        });
        builder.setNegativeButton(Res.str().cancel(), (dialogInterface, i) -> {
        });

        AlertDialog dialog = builder.create();
        final EditText input = new EditText(context);

        input.setInputType(inputType);
        dialog.setView(input);
        input.setText(s.getValueAsString());

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            try {
                s.setValueFromString(input.getText().toString());
                onClick.onClick(view);
                dialog.dismiss();

            } catch (ValidationException e) {
                input.setError(e.getMessage());
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener( view -> dialog.dismiss());

    }



    public SolidTextInputDialog(Context c, final AbsSolidType s, int inputType) {
        this(c,s, inputType, v -> {

        });
    }
}

