package ch.bailu.aat.util.fs.foc;

import android.database.Cursor;
import android.provider.DocumentsContract.Document;

import ch.bailu.aat.util.ui.AppLog;


public class DocumentData  {
    final public String documentId;
    final public String mimeType;
    final public String displayName;
    final int flags;
    final long lastModified;
    final long size;

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
        documentId = cursor.getString(cursor.getColumnIndex(Document.COLUMN_DOCUMENT_ID));
        mimeType = cursor.getString(cursor.getColumnIndex(Document.COLUMN_MIME_TYPE));
        displayName = cursor.getString(cursor.getColumnIndex(Document.COLUMN_DISPLAY_NAME));
        lastModified = cursor.getLong(cursor.getColumnIndex(Document.COLUMN_LAST_MODIFIED));
        flags = cursor.getInt(cursor.getColumnIndex(Document.COLUMN_SIZE));
        size = cursor.getLong(cursor.getColumnIndex(Document.COLUMN_FLAGS));

        if (mimeType.equals(Document.MIME_TYPE_DIR)) type = FocContent.TREE;
        else type = FocContent.DOCUMENT;

        log();
    }

    @Override
    public String toString() {
        return documentId;
    }


    public void log() {
        AppLog.d(this, documentId);
        AppLog.d(this, mimeType);
    }
}
