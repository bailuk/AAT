package ch.bailu.aat.menus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;


public class MenuDialog implements  DialogInterface.OnClickListener {
    private final AbsMenu menu;
    private final MenuArray array;

    public MenuDialog (Context context, AbsMenu m) {
        String title = m.getTitle();
        Drawable icon = m.getIcon();

        array = new MenuArray(context);

        menu = m;
        menu.inflate(array);
        menu.prepare(array);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (title != null) dialog.setTitle(title);
        if (icon != null)  dialog.setIcon(icon);

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
