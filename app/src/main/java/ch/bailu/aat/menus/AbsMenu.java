package ch.bailu.aat.menus;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.util.ArrayList;

public abstract class AbsMenu {
    public abstract void inflate(Menu menu);

    public abstract String getTitle();
    public abstract Drawable getIcon();

    public abstract void prepare(Menu menu);

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

        popup.setOnMenuItemClickListener(this::onItemClick);

        popup.show();
    }


    public abstract static class Item {
        private final MenuItem item;

        public Item(Menu menu, int id) {
            item = menu.add(id);
        }

        public Item(Menu menu, String name) {
            item = menu.add(name);
        }

        public abstract void onClick();

        public boolean onClick(MenuItem item) {
            boolean result = (this.item == item);

            if (result) {
                onClick();
            }
            return result;
        }
    }

    private final ArrayList<Item> items = new ArrayList<>(10);
    public void add(Item item) {
        items.add(item);
    }

    public boolean onItemClick(MenuItem item) {
        for (Item i: items) {
            if (i.onClick(item)) return true;
        }
        return false;
    }
}
