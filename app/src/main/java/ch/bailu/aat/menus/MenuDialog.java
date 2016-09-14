package ch.bailu.aat.menus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.File;

import ch.bailu.aat.preferences.SolidOverlayFileList;


public class MenuDialog implements  DialogInterface.OnClickListener {
    private final AlertDialog.Builder dialog;
    private final AbsMenu menu;
    private final MenuArray array;

    public MenuDialog (Context context, AbsMenu m) {
        array = new MenuArray(context);

        menu = m;
        menu.inflate(array);
        menu.prepare(array);

        dialog = new AlertDialog.Builder(context);
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
