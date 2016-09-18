package ch.bailu.aat.menus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class MenuDialog implements  DialogInterface.OnClickListener {
    private final AbsMenu menu;
    private final MenuArray array;

    public MenuDialog (Context context, AbsMenu m) {
        array = new MenuArray(context);

        menu = m;
        menu.inflate(array);
        menu.prepare(array);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setItems(array.toStringArray(), this);
        dialog.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        menu.onItemClick(array.getItem(i));
        dialog.dismiss();
    }



}
