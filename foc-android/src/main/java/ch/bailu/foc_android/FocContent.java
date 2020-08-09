package ch.bailu.foc_android;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import ch.bailu.foc.Foc;

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

@TargetApi(FocAndroid.SAF_MIN_SDK)
public class FocContent extends Foc {

    private DocumentData data = null;

    private final ContentResolver resolver;
    private final LazyUris uris;




    // called from parent
    private FocContent(ContentResolver r, LazyUris u, DocumentData d) {
        this (r, u);
        data = d;
    }


    // called from child
    private FocContent(ContentResolver r, LazyUris u) {
        resolver = r;
        uris = u;
    }


    // called from factory
    public FocContent(ContentResolver r, Uri per, DocumentId id) {
        resolver = r;
        uris = new LazyUris(per, id);
    }




    @Override
    public boolean move(Foc dest) throws IOException, SecurityException {

        boolean ok = false;

        if (dest.parent().equals(parent())) {
            ok = dest.getName().equals(getName());
            ok = ok || (DocumentsContract.renameDocument(resolver, uris.getDocument(), dest.getName()) != null);
        }
        return ok || super.move(dest);

    }

    @Override
    public boolean remove() throws IOException, SecurityException {
        if (childCount() == 0) {
            return DocumentsContract.deleteDocument(resolver, uris.getDocument());
        }

        return false;
    }


    @Override
    public boolean mkdir() {
        return createDocument(DocumentsContract.Document.MIME_TYPE_DIR) && isDir();
    }


    private boolean createPath(String mimeType) {
        return  (exists() || (mkParents() && createDocument(mimeType)));
    }


    private boolean createDocument(String mimeType) {

        if (exists()) {
            return true;

        } else {
//            AppLog.d(this, "create: " + mimeType);
            update();

            return createDocumentNoException(mimeType) || exists();
        }
    }

    private boolean createDocumentNoException(String mimeType) {
        try {
            return DocumentsContract.createDocument(
                    resolver,
                    uris.getParent(),
                    mimeType,
                    getName()) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasParent() {
        return uris.hasParent();
    }


    @Override
    public Foc parent() {
        if (uris.hasParent()) return new FocContent(resolver, uris.parent());
        return null;
    }


    @Override
    public Foc child(String name) {
        return new FocContent(resolver, uris.child(uris.getDocumentId().child(name)));
    }






    @Override
    public void foreach(Execute exec) {
        if (isFile()) return;

        Cursor cursor = null;
        try {
            cursor = resolver.query(uris.getChild(),null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    DocumentData data = new DocumentData(cursor);
                    exec.execute(new FocContent(resolver, uris.child(new DocumentId(data.documentId)), data));

                } while (cursor.moveToNext());
            }
        } catch(Exception e) {
            Log.w(getClass().getSimpleName(), e.toString());
        }

        if (cursor != null) cursor.close();
    }


    private static class ChildCount extends Execute {
        public int count = 0;
        @Override
        public void execute(Foc child) {
            count++;
        }
    }

    private int childCount() {
        ChildCount children = new ChildCount();
        foreach(children);
        return children.count;
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
        querySelf();
        return Objects.equals(data.type, DocumentData.TREE);
    }

    @Override
    public boolean isFile() {
        querySelf();
        return Objects.equals(data.type, DocumentData.DOCUMENT);
    }

    @Override
    public boolean exists() {
        querySelf();
        return data.exists;
    }

    @Override
    public boolean canRead() {
        querySelf();
        return data.exists;
    }

    @Override
    public boolean canWrite() {
        querySelf();
        return (data.flags & DocumentsContract.Document.FLAG_SUPPORTS_WRITE)
                == DocumentsContract.Document.FLAG_SUPPORTS_WRITE ||
                (data.flags & DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE)
                == DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE;
    }

    @Override
    public long length() {
        if (isDir()) return 0;

        querySelf();
        return data.size;
    }


    @Override
    public String getPath() {
        return uris.getDocument().toString();
    }


    @Override
    public long lastModified() {
        querySelf();
        return data.lastModified;
    }


    @Override
    public InputStream openR() throws IOException, SecurityException {
        return resolver.openInputStream(uris.getDocument());
    }

    @Override
    public OutputStream openW() throws IOException, SecurityException {
        createPath(MimeType.fromName(getName()));

        OutputStream r = resolver.openOutputStream(uris.getDocument());

        if (r==null) throw new IOException(getPathName());
        else return r;
    }

    public void update() {
        data = null;
    }


    private void querySelf() {
        if (data != null) return;

        Cursor cursor = null;
        try {
            cursor = resolver.query(uris.getDocument(),null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                data = new DocumentData(cursor);

            } else {
                //AppLog.d(this, uris.getDocument().toString());

                data = new DocumentData(uris.getDocumentId().toString());
            }

        } catch(Exception e) {
            data = new DocumentData(uris.getDocumentId().toString());
        }
        Foc.close(cursor);
    }


    @Override
    public String getName() {
        return uris.getDocumentId().getName();
    }

    @Override
    public String getPathName() {
        return uris.getDocumentId().toString();
    }

}
