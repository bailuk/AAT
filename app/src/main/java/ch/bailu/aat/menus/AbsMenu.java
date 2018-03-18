package ch.bailu.aat.menus;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

public abstract class AbsMenu {
    public abstract void inflate(Menu menu);

    public void inflateWithHeader(ContextMenu menu) {
        Drawable icon = getIcon();
        String title = getTitle();


        if (title != null)
            menu.setHeaderTitle(title);

        if (icon != null)
            menu.setHeaderIcon(icon);

        inflate(menu);

    }

    public abstract String getTitle();
    public abstract Drawable getIcon();

    public abstract void prepare(Menu menu);

    public abstract boolean onItemClick(MenuItem item);


    public void showAsPopup (Context context, View view) {
        showAsPopupSDK11(context, view);
    }



    public void showAsDialog(Context context) {
        new MenuDialog(context, this);
    }


    @TargetApi(11)
    private void showAsPopupSDK11(Context context, View view) {
        final PopupMenu popup = new PopupMenu(context, view);

        inflate(popup.getMenu());
        prepare(popup.getMenu());
        
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onItemClick(item);

            }});

        popup.show();
    }

}
