package ch.bailu.aat_gtk.lib.menu;

import java.util.ArrayList;

import ch.bailu.aat_gtk.lib.Resources;
import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gio.MenuItem;
import ch.bailu.gtk.glib.Variant;
import ch.bailu.gtk.type.Str;

public class MenuModelBuilder {
    private final ArrayList<Item> items = new ArrayList<>();

    public void clear() {
        items.clear();
    }

    public interface OnActivated {
        void onActivated(int index);
    }

    public interface OnSelected {
        void onSelected();
    }

    public interface OnChecked {
        void onChecked(boolean enabled);
    }

    private abstract class Item {
        final ActionId actionId = new ActionId();
        final String label;
        public Item(String label) {
            this.label = label;
        }
        abstract void appendTo(Menu menu, Actions actions, Resources res);
    }

    private class LabelItem extends Item {
        final OnSelected onSelected;
        public LabelItem(String label, OnSelected onSelected) {
            super(label);
            this.onSelected = onSelected;
        }

        @Override
        void appendTo(Menu menu, Actions actions, Resources res) {
            menu.appendItem(new MenuItem(res.str(label), res.str("app." + actionId.get())));
            actions.add(actionId.get(), parameter -> onSelected.onSelected());
        }
    }

    private class CustomItem extends Item {
        public CustomItem(String id) {
            super(id);
        }

        @Override
        void appendTo(Menu menu, Actions actions, Resources res) {
            var item = new MenuItem(res.str(label), res.str("app." + actionId.get()));
            item.setAttribute(res.str("custom"), null);
            item.setAttributeValue(res.str("custom"), Variant.newStringVariant(res.str(label)));
            menu.appendItem(item);
        }
    }


    private class CheckItem extends Item {
        boolean selected;
        final OnChecked onChecked;

        public CheckItem(String label, boolean selected, OnChecked onChecked) {
            super(label);
            this.selected = selected;
            this.onChecked = onChecked;
        }

        @Override
        void appendTo(Menu menu, Actions actions, Resources res) {
            menu.appendItem(new MenuItem(new Str(label), res.str("app." + actionId.get())));
            actions.add(actionId.get(), parameter -> {
                if (parameter != null) {
                    onChecked.onChecked(GTK.IS(parameter.getBoolean()));
                }
            });
        }
    }



    private class SubmenuItem extends Item {
        final MenuModelBuilder submenu;

        public SubmenuItem(String label, MenuModelBuilder submenu) {
            super(label);
            this.submenu = submenu;
        }

        @Override
        void appendTo(Menu menu, Actions actions, Resources res) {
            menu.appendSubmenu(strLabel(), submenu.create(actions, res));
        }

        public Str strLabel() {
            if (label == null || label.length() == 0) {
                return null;
            }
            return new Str(label);
        }
    }

    private class SeparatorItem extends SubmenuItem {
        public SeparatorItem(String label, MenuModelBuilder submenu) {
            super(label, submenu);
        }

        @Override
        void appendTo(Menu menu, Actions actions, Resources res) {
            menu.appendSection(strLabel(), submenu.create(actions, res));
        }

    }

    private class RadioGroup extends Item {
        private final ArrayList<String> radios = new ArrayList<>();
        private final OnActivated onActivated;
        private final int initialIndex;

        public RadioGroup(OnActivated onActivated, int initalIndex) {
            super("");
            this.onActivated = onActivated;
            this.initialIndex = initalIndex;
        }

        public void add(String label) {
            radios.add(label);
        }

        @Override
        void appendTo(Menu menu, Actions actions, Resources res) {
            addItems(menu);
            addAction(actions);
        }

        void addAction(Actions actions) {
            actions.add(actionId.get(), initialIndex, parameter -> {
                if (parameter != null) {
                    var index = parameter.getInt32();
                    if (index < radios.size()) {
                        onActivated.onActivated(index);
                    }
                }
            });
        }

        void addItems(Menu menu) {
            for (int i = 0; i< radios.size(); i++) {
                var item = new MenuItem(new Str(radios.get(i)), new Str("app." + actionId.get(i)));
                menu.appendItem(item);
            }
        }
    }

    public MenuModelBuilder label(String label, OnSelected onSelected) {
        items.add(new LabelItem(label, onSelected));
        return this;
    }

    public MenuModelBuilder submenu(String label, MenuModelBuilder submenu) {
        items.add(new SubmenuItem(label, submenu));
        return this;
    }

    public MenuModelBuilder separator(String label, MenuModelBuilder submenu) {
        items.add(new SeparatorItem(label, submenu));
        return this;
    }

    private RadioGroup currentGroup = null;

    public MenuModelBuilder radioGroup(OnActivated onActivated, int initialIndex) {
        currentGroup = new RadioGroup(onActivated, initialIndex);
        return this;
    }

    public MenuModelBuilder radio(String label) {
        if (currentGroup != null) currentGroup.add(label);
        return this;
    }

    public MenuModelBuilder custom(String id) {
        items.add(new CustomItem(id));
        return this;
    }

    public MenuModelBuilder check(String label, boolean selected, OnChecked onChecked) {
        items.add(new CheckItem(label, selected, onChecked));
        return this;
    }

    public Menu create(Actions actions, Resources res) {
        final var result = new Menu();

        for (Item item : items) {
            item.appendTo(result, actions, res);
        }
        return result;
    }
}
