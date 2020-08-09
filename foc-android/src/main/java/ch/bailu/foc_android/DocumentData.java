package ch.bailu.foc_android;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.provider.DocumentsContract.Document;
import android.util.Log;

import androidx.annotation.NonNull;


@TargetApi(FocAndroid.SAF_MIN_SDK)
public class DocumentData  {
    final public String documentId;
    final public String mimeType;
    final public String displayName;
    final public int flags;
    final public long lastModified;
    final public long size;
    final public boolean exists;

    public final static String TREE= "tree";
    public final static String DOCUMENT="document";
    public final static String UNKNOWN="unknown";

    final String type;


    public DocumentData(String id) {
        documentId = id;
        mimeType="";
        displayName=id;
        lastModified=System.currentTimeMillis();
        flags = 0;
        size = 0;
        type = UNKNOWN;
        exists=false;
    }

    public DocumentData(Cursor cursor) {
        documentId = cursor.getString(index(cursor, Document.COLUMN_DOCUMENT_ID));
        mimeType = cursor.getString(index(cursor, Document.COLUMN_MIME_TYPE));
        displayName = cursor.getString(index(cursor, Document.COLUMN_DISPLAY_NAME));
        lastModified = cursor.getLong(index(cursor, Document.COLUMN_LAST_MODIFIED));
        flags = cursor.getInt(index(cursor, Document.COLUMN_FLAGS));



        size = cursor.getLong(index(cursor, Document.COLUMN_SIZE));

        if (mimeType.equals(Document.MIME_TYPE_DIR)) type = TREE;
        else type = DOCUMENT;

        exists=true;
    }


    private int index(Cursor cursor, String columnDocumentId) {
        int i = cursor.getColumnIndex(columnDocumentId);

        if (cursor.isNull(i)) {
            Log.w(getClass().getSimpleName(), columnDocumentId + " is null");
        }
        return i;
    }


    @NonNull
    @Override
    public String toString() {
        return documentId;
    }
}
