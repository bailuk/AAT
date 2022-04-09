package ch.bailu.aat_gtk.lib.menu;


import ch.bailu.aat_gtk.lib.Resources;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gio.Menu;

public class MenuFacade {
    private Menu menu = null;
    private final MenuModelBuilder modelBuilder = new MenuModelBuilder();

    private final Actions actions;
    private final Resources resources = new Resources();

    public MenuFacade(Application app) {
        this.actions = new Actions(app);
    }

    public MenuModelBuilder build() {
        return modelBuilder;
    }

    public Menu getModel() {
        if (menu == null) {
            menu = this.modelBuilder.create(actions, resources);
        }
        return menu;
    }

    public void destroy() {
        if (menu != null) {
            menu.unref();
            actions.destroy();
            resources.destroy();
            menu = null;
        }
    }
}
