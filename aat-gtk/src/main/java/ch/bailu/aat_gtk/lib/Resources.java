package ch.bailu.aat_gtk.lib;


import java.util.HashSet;
import java.util.Set;

import ch.bailu.gtk.Refs;
import ch.bailu.gtk.type.Array;
import ch.bailu.gtk.type.Str;

public class Resources {

    private final Set<Object> refs = new HashSet<>();

    public synchronized void add(Object object) {
        refs.add(object);
    }

    public synchronized void destroy() {
        refs.forEach(o -> {
            if (o instanceof Array) {
                ((Array) o).destroy();
            }

            if (o instanceof ch.bailu.gtk.gobject.Object) {
                ((ch.bailu.gtk.gobject.Object) o).unref();
            }

            Refs.remove(o);
        });
        refs.clear();
    }

    public Str str(String string) {
        Str result = new Str(string);
        add(string);
        return result;
    }
}
