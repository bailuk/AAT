package ch.bailu.foc_android;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.foc.Foc;

public class FocUri extends Foc {
    final Uri uri;
    final ContentResolver resolver;

    public FocUri(ContentResolver r, Uri u) {
        uri = u;
        resolver = r;
    }


    @Override
    public boolean remove() {
        return resolver.delete(uri,null,null) > 0;
    }

    @Override
    public boolean mkdir() {
        return false;
    }

    @Override
    public Foc parent() {
        return null;
    }

    @Override
    public Foc child(String name) {

        return new FocUri(resolver, Uri.parse(this.toString() + "/" + name));
    }

    @Override
    public String getName() {
        return uri.getLastPathSegment();
    }

    @Override
    public String getPath() {
        return uri.toString();
    }

    @Override
    public long length() {
        return 0;
    }


    @Override
    public void foreach(Execute e) {

    }

    @Override
    public void foreachFile(Execute e) {

    }

    @Override
    public void foreachDir(Execute e) {

    }

    @Override
    public boolean isDir() {
        return false;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean canRead() {
        return false;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public long lastModified() {
        return System.currentTimeMillis();
    }

    @Override
    public InputStream openR() throws IOException {
        return resolver.openInputStream(uri);
    }

    @Override
    public OutputStream openW() throws IOException {
        return resolver.openOutputStream(uri);
    }
}
