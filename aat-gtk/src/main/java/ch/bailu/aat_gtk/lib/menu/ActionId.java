package ch.bailu.aat_gtk.lib.menu;

public class ActionId {
    private static int ids = 0;
    private final int id;

    public ActionId() {
        id = ids++;
    }

    public String get() {
        return "menu_" + id;
    }
    public String get(int index) {
        return "menu_" + id + "(" + index + ")";
    }

}
