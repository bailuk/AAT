package ch.bailu.aat.util.fs.foc;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.provider.DocumentsContract.Document;
import android.provider.OpenableColumns;

import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.util.ui.AppLog;


public class DocumentData  {
    final public String documentId;
    final public String mimeType;
    final public String displayName;
    final public int flags;
    final public long lastModified;
    final public long size;

    final String type;


    public DocumentData(String id) {
        documentId = id;
        mimeType="";
        displayName=id;
        lastModified=System.currentTimeMillis();
        flags = 0;
        size = 0;
        type = FocContent.UNKNOWN;
    }

    public DocumentData(Cursor cursor) {
        documentId = cursor.getString(index(cursor, Document.COLUMN_DOCUMENT_ID));
        mimeType = cursor.getString(index(cursor, Document.COLUMN_MIME_TYPE));
        displayName = cursor.getString(index(cursor, Document.COLUMN_DISPLAY_NAME));
        lastModified = cursor.getLong(index(cursor, Document.COLUMN_LAST_MODIFIED));
        flags = cursor.getInt(index(cursor, Document.COLUMN_FLAGS));
        size = cursor.getLong(index(cursor, Document.COLUMN_SIZE));

        if (mimeType.equals(Document.MIME_TYPE_DIR)) type = FocContent.TREE;
        else type = FocContent.DOCUMENT;


    }


    private int index(Cursor cursor, String columnDocumentId) {
        int i = cursor.getColumnIndex(columnDocumentId);


        if (cursor.isNull(i)) {
            AppLog.d(this, columnDocumentId + " is null");
        }
        return i;
    }


    @Override
    public String toString() {
        return documentId;
    }
}
