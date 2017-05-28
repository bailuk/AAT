package ch.bailu.aat.util.fs.foc;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;

/**
 *
 * Android document uri
 *
 * content://[authority]/[tree|document]/[document ID]
 * [scheme]://[authority]/[uri type]/[document ID]/[document type]/[document ID]

 *
 * uri type: uri
 *
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class FocContent extends Foc {
    final static String TREE="tree";
    final static String DOCUMENT="document";
    final static String UNKNOWN="unknown";

    private String type;

    final String authority;
    final Uri tree, document, child, parent;
    final String encodedDocumentId;
    final String documentId;

    DocumentData data;

    final ContentResolver resolver;

    public FocContent(ContentResolver r, Uri uri, DocumentData d) {
        this (r,uri, d.type);
        data = d;
    }


    public FocContent(ContentResolver r, Uri uri, String t) {
        resolver = r;
        type = t;


        authority = uri.getAuthority();
        encodedDocumentId = getDocumentId(uri);
        documentId = Uri.decode(encodedDocumentId);

        tree = DocumentsContract.buildTreeDocumentUri(authority, documentId);
        document = DocumentsContract.buildDocumentUri(authority, documentId);
        child = DocumentsContract.buildChildDocumentsUriUsingTree(tree, documentId);

        parent = DocumentsContract.buildTreeDocumentUri(authority, getParentId(documentId));


    }

    private String getDocumentId(Uri uri) {
        List <String> s = uri.getPathSegments();
        if (s.size()>1) {
            return s.get(1);
        }
        return "";
    }

    private String getParentId(String doc) {
        if (doc != null) {
            int lastIndex = doc.length();
            while (lastIndex > 0) {
                lastIndex--;
                if (doc.charAt(lastIndex) == '/')
                    return doc.substring(0, lastIndex);
            }
        }
        return doc;
    }


    @Override
    public boolean remove() throws IOException, SecurityException {
        return false;
    }

    @Override
    public boolean mkdir() {
        return false;
    }

    @Override
    public Foc parent() {
        if (parent.equals(tree))
            return null;

        return new FocContent(resolver, parent, TREE);
    }

    @Override
    public Foc child(String name) {
        String u = tree.toString() + Uri.encode("/" + name);
        return new FocContent(resolver, Uri.parse(u), UNKNOWN);
    }


    @Override
    public String getName() {
        return documentId;
    }



    private void querySelf() {
        if (data != null) return;

        Cursor cursor = null;
        try {
            cursor = resolver.query(document,null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                data = new DocumentData(cursor);
                type = data.type;

                AppLog.d(this, data.toString());
            }
        } catch(Exception e) {
            data = new DocumentData(documentId);
        }
        if (cursor != null) cursor.close();
    }


    @Override
    public void foreach(Execute exec) {

        if (isFile()) return;

        Cursor cursor = null;
        try {
            cursor = resolver.query(child,null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                type = TREE;

                do {
                    DocumentData data = new DocumentData(cursor);
                    AppLog.d(this, data.mimeType);
                    Uri child=DocumentsContract.buildTreeDocumentUri(authority, data.documentId);
                    exec.execute(new FocContent(resolver, child, data));

                } while (cursor.moveToNext());
            }
        } catch(Exception e) {}

        if (cursor != null) cursor.close();

    }

    @Override
    public void foreachFile(final Execute e) {
        foreach(new Execute() {
            @Override
            public void execute(Foc child) {
                if (child.isFile()) e.execute(child);
            }
        });
    }

    @Override
    public void foreachDir(final Execute e) {
        foreach(new Execute() {
            @Override
            public void execute(Foc child) {
                if (child.isDir()) e.execute(child);
            }
        });

    }

    @Override
    public boolean isDir() {
        return type == TREE;
    }

    @Override
    public boolean isFile() {
        return type == DOCUMENT;
    }

    @Override
    public boolean exists() {
        querySelf();
        return type != UNKNOWN;
    }

    @Override
    public boolean canRead() {
        querySelf();
        return exists();
    }

    @Override
    public boolean canWrite() {
        querySelf();

        return (data.flags & DocumentsContract.Document.FLAG_SUPPORTS_WRITE) == data.flags;
    }

    @Override
    public long length() {
        if (isDir()) return 0;

        querySelf();
        return data.size;
    }

    @Override
    public String toString() {
        if (type == DOCUMENT) return document.toString();
        return tree.toString();
    }

    @Override
    public long lastModified() {

        querySelf();
        return data.lastModified;
    }


    @Override
    public InputStream openR() throws IOException {
        return resolver.openInputStream(document);
    }

    @Override
    public OutputStream openW() throws IOException {
        return resolver.openOutputStream(document);
    }
}
