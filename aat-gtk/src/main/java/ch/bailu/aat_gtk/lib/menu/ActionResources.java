package ch.bailu.aat_gtk.lib.menu;

import java.util.HashMap;

import ch.bailu.aat_gtk.lib.Resources;
import ch.bailu.gtk.gio.Action;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.Application;
import ch.bailu.gtk.gio.SimpleAction;

public class ActionResources {

    private final ActionMap map;
    private final HashMap<String, Action> actions = new HashMap<>();
    private final HashMap<String, Resources> resources = new HashMap<>();


    public ActionResources(Application app) {
        this.map = new ActionMap(app.cast());
    }


    public void add(String name, SimpleAction simpleAction, Resources res) {
        var action = new Action(simpleAction.cast());

        map.addAction(action);
        actions.put(name, action);
        resources.put(name, res);
    }

    public void remove(String name) {
        var res = resources.remove(name);
        if (res != null) {
            map.removeAction(res.str(name));
            actions.remove(name);
            res.destroy();
        }
    }

    Action get(String name) {
        return actions.get(name);
    }

    public void destroy() {
        var keys = actions.keySet();
        for (var k : keys.toArray(new String[0])) {
            remove(k);
        }
    }
}
