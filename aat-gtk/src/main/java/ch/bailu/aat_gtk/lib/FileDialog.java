package ch.bailu.aat_gtk.lib;

import ch.bailu.gtk.Refs;
import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.Dialog;
import ch.bailu.gtk.gtk.FileChooser;
import ch.bailu.gtk.gtk.FileChooserAction;
import ch.bailu.gtk.gtk.ResponseType;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;

public class FileDialog {

    private Str label = Str.NULL;
    private Str cancel = new Str("Cancel");
    private Str ok = new Str("OK");
    private Str initialPath = Str.NULL;

    private int action = FileChooserAction.OPEN;

    public interface Response {
        void onResponse(String path);
    }

    private Response response = path -> {};

    public FileDialog label(String label) {
        this.label.destroy();
        this.label = new Str(label);
        return this;
    }

    public FileDialog ok(String label) {
        this.ok.destroy();
        this.ok = new Str(label);
        return this;
    }

    public FileDialog cancel(String label) {
        this.cancel.destroy();
        this.cancel = new Str(label);
        return this;
    }

    public FileDialog open() {
        this.action = FileChooserAction.OPEN;
        return this;
    }

    public FileDialog save() {
        this.action = FileChooserAction.SAVE;
        return this;
    }

    public FileDialog response(Response response) {
        this.response = response;
        return this;
    }

    public FileDialog selectFolder() {
        this.action = FileChooserAction.SELECT_FOLDER;
        return this;
    }

    public FileDialog path(String path) {
        this.initialPath.destroy();
        this.initialPath = new Str(path);
        return this;
    }

    private Dialog.OnResponse onResponse = i -> {
        String path = "";
        if (i == ResponseType.OK) {
            path = getPath();
        }

        destroy();
        response.onResponse(path);
    };

    private String getPath() {
        String result = "";
        if (dialog != null) {
            var chooser = new FileChooser(dialog.cast());
            var file = chooser.getFile();
            var path = file.getPath();

            result = path.toString();
            unref(file);
            path.destroy();
        }
        return result;
    }


    private ch.bailu.gtk.gtk.FileChooserDialog dialog;

    public void show(Window window) {
        dialog = new ch.bailu.gtk.gtk.FileChooserDialog(label, window, action, null);

        setInitialPath();
        dialog.addButton(cancel, ResponseType.CANCEL);
        dialog.addButton(ok, ResponseType.OK);
        dialog.onResponse(onResponse);
        dialog.show();
    }

    private void setInitialPath() {
        if (dialog != null && initialPath.getSize() > 0) {
            try {
                var path = File.newForPath(initialPath);
                new FileChooser(dialog.cast()).setFile(path);
                unref(path);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void unref(File file) {
        new ch.bailu.gtk.gobject.Object(file.cast()).unref();
    }

    private void destroy() {
        if (dialog != null) {
            dialog.close();
            dialog = null;
        }

        label.destroy();
        cancel.destroy();
        ok.destroy();
        initialPath.destroy();
        Refs.remove(onResponse);
    }
}
