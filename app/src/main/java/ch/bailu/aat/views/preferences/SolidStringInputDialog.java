package ch.bailu.aat.views.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ch.bailu.aat.R;
import ch.bailu.aat.exception.ValidationException;
import ch.bailu.aat.preferences.AbsSolidType;

public class SolidStringInputDialog extends AbsSolidDialog {

    public SolidStringInputDialog(final AbsSolidType s) {

        final Context context = s.getContext();
        final AlertDialog.Builder builder = createDefaultDialog(s);

        builder.setPositiveButton(context.getString(R.string.ok), (dialogInterface, i) -> {
        });
        builder.setNegativeButton(context.getString(R.string.cancel), (dialogInterface, i) -> {
        });

        AlertDialog dialog = builder.create();
        final EditText input = new EditText(context);
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
