package ch.bailu.foc_android;

import android.util.Log;

import androidx.annotation.NonNull;

public class DocumentId {
    final private String documentId;

    public DocumentId(String did) {
        documentId = did;
    }

    @NonNull
    @Override
    public String toString() {
        return documentId;
    }

    public DocumentId parent() {
        int lastIndex = documentId.length();
        while (lastIndex > 0) {
            lastIndex--;
            if (documentId.charAt(lastIndex) == '/' || documentId.charAt(lastIndex) == ':')
                return new DocumentId(documentId.substring(0, lastIndex));
        }
        return this;
    }


    public DocumentId child(String child) {

        if (child.length() == 0) {
            return this;
        }

        if (child.charAt(child.length()-1) == '/') {
            Log.w(getClass().getSimpleName(), child + " ends with '/'");
            return child(child.substring(0,child.length()-1));
        }

        if (child.charAt(0) == '/') {
            return new DocumentId(documentId + child);
        } else {
            return new DocumentId(documentId + "/" + child);
        }
    }


    public String getName() {
        int beginIndex = documentId.length()-1;

        while (beginIndex > 0) {
            beginIndex--;

            if (documentId.charAt(beginIndex) == '/' || documentId.charAt(beginIndex) == ':') {
                return documentId.substring(beginIndex+1);
            }
        }

        return documentId;
    }
}
