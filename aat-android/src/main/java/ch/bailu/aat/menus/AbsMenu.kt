package ch.bailu.aat.menus;

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

    private void showAsPopupSDK11(Context context, View view) {
        final PopupMenu popup = new PopupMenu(context, view);

        inflate(popup.getMenu());
        prepare(popup.getMenu());

        popup.setOnMenuItemClickListener(this::onItemClick);

        popup.show();
    }

    public interface OnClick {
        void onClick();
    }

    private static class Item {
        private final MenuItem item;
        private final OnClick onClick;

        public Item(MenuItem item, OnClick onClick) {
            this.item = item;
            this.onClick = onClick;
        }

        public boolean onClick(MenuItem item) {
            boolean result = (this.item == item);

            if (result) {
                onClick.onClick();
            }
            return result;
        }
    }

    private final ArrayList<Item> items = new ArrayList<>(5);
    public MenuItem add(Menu menu, String name, OnClick onClick) {
        MenuItem result = menu.add(name);
        items.add(new Item(result, onClick));
        return  result;
    }

    public MenuItem add(Menu menu, int nameID, OnClick onClick) {
        MenuItem result = menu.add(nameID);
        items.add(new Item(result, onClick));
        return  result;
    }

    public boolean onItemClick(MenuItem item) {
        for (Item i: items) {
            if (i.onClick(item)) return true;
        }
        return false;
    }
}
