package ch.bailu.aat_gtk.lib;

import java.io.IOException;

import ch.bailu.gtk.gio.Resource;
import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.Bytes;
import ch.bailu.gtk.type.exception.AllocationError;

public class GResource {

    /**
     * Load a gresource bundle from java resources path and register it
     * See  {@link ch.bailu.gtk.gio.Resource} for documentation on how to generate
     * gresource bundles.
     *
     * @param path absolute path to gresource: "/gresource/app.gresource"
     */
    public static void loadAndRegister(String path) {
        try (var stream = (new JavaResource(path).asStream())) {
            var bytes = new Bytes(stream.readAllBytes());
            var resource = Resource.newFromDataResource(ch.bailu.gtk.glib.Bytes.newStaticBytes(bytes, bytes.getLength()));
            resource.register();
        } catch (IOException | AllocationError e) {
            System.err.println("Load gresource failed for '"  + path + "'");
        }
    }
}
