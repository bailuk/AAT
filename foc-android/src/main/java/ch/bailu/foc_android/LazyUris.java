package ch.bailu.foc_android;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LazyUris {
    private final Uri permission;

    private Uri parent, document, child;

    private final DocumentId documentId;
    private DocumentId parentId;



    public LazyUris(Uri per, DocumentId id) {
        permission = per;
        documentId = id;
    }

    public LazyUris parent() {
       return new LazyUris(permission, getParentId());
    }

    public LazyUris child(DocumentId childId) {
        LazyUris r = new LazyUris(permission, childId);
        r.parentId = documentId;
        r.parent = document;
        return r;
    }


    public Uri getParent() {
        if (parent == null) {
            if (hasParent()) {
                parent = DocumentsContract.buildDocumentUriUsingTree(permission, getParentId().toString());
            } else {
                parent = document;
            }
        }
        return parent;
    }


    public Uri getChild() {
        if (child == null) {
            child = DocumentsContract.buildChildDocumentsUriUsingTree(permission, documentId.toString());
        }
        return child;
    }

    public Uri getDocument() {
        if (document == null) {
            document = DocumentsContract.buildDocumentUriUsingTree(permission, documentId.toString());
        }
        return document;
    }

    public DocumentId getParentId() {
        if (parentId == null) parentId = documentId.parent();
        return parentId;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public boolean hasParent() {
        return (getParentId() != getDocumentId());
    }

    /*
    public void log() {

        AppLog.d(this, "Document: " + getDocument());
        AppLog.d(this, "Parent: " + getParent());
        AppLog.d(this, "Child: " + getChild());
        AppLog.d(this, "ParentId: " + getParentId());
        AppLog.d(this, "DocumentId: " + getDocumentId());

    }
    */
}
