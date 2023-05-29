package ch.bailu.aat.providers;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract.Document;
import android.provider.DocumentsContract.Root;
import android.provider.DocumentsProvider;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat_lib.preferences.general.SolidPresetCount;
import ch.bailu.aat_lib.preferences.presets.SolidMET;
import ch.bailu.aat_lib.util.Objects;
import ch.bailu.aat_lib.util.fs.AppDirectory;

public class GpxDocumentProvider extends DocumentsProvider {

    private static final String[] DEFAULT_ROOT_PROJECTION =
            new String[]{Root.COLUMN_ROOT_ID, Root.COLUMN_MIME_TYPES,
                    Root.COLUMN_FLAGS, Root.COLUMN_ICON, Root.COLUMN_TITLE,
                    Root.COLUMN_SUMMARY, Root.COLUMN_DOCUMENT_ID,
                    Root.COLUMN_AVAILABLE_BYTES,};
    private static final String[] DEFAULT_DOCUMENT_PROJECTION = new
            String[]{Document.COLUMN_DOCUMENT_ID, Document.COLUMN_MIME_TYPE,
            Document.COLUMN_DISPLAY_NAME, Document.COLUMN_LAST_MODIFIED,
            Document.COLUMN_FLAGS, Document.COLUMN_SIZE,};
    private static final String ROOT = "root";
    private static final String DIR_PREFIX = "dir";
    private static final String GPX_PREFIX = "gpx";
    private static final String GPX_INFIX = "_";


    @Override
    public Cursor queryRoots(String[] projection) {
        final MatrixCursor result = new MatrixCursor(projection == null ? DEFAULT_ROOT_PROJECTION : projection);
        result.newRow()
                .add(Root.COLUMN_ROOT_ID, ROOT)
                .add(Root.COLUMN_ICON, R.mipmap.ic_launcher)
                .add(Root.COLUMN_FLAGS, Root.FLAG_SUPPORTS_RECENTS)
                .add(Root.COLUMN_TITLE, "AAT GPX")
                .add(Root.COLUMN_DOCUMENT_ID, ROOT)
                .add(Root.COLUMN_MIME_TYPES, new HashSet<>(List.of("application/gpx+xml")));
        return result;
    }

    private void includeFile(MatrixCursor result, int preset, File file) {
        result.newRow()
                .add(Document.COLUMN_DOCUMENT_ID, GPX_PREFIX + preset + GPX_INFIX + file.getName())
                .add(Document.COLUMN_DISPLAY_NAME, file.getName())
                .add(Document.COLUMN_MIME_TYPE, "application/gpx+xml")
                .add(Document.COLUMN_FLAGS, 0)
                .add(Document.COLUMN_LAST_MODIFIED, file.lastModified())
                .add(Document.COLUMN_SIZE, file.length());
    }

    private void includeDirectory(MatrixCursor result, int preset) {
        final Context context = getContext();
        final String presetLabel = context.getString(R.string.p_preset);
        final File file = new File(AppDirectory.getTrackListDirectory(new AndroidSolidDataDirectory(context), preset).getPath());
        final String met = new SolidMET(new Storage(context), preset).getValueAsString();
        result.newRow()
                .add(Document.COLUMN_DOCUMENT_ID, DIR_PREFIX + preset)
                .add(Document.COLUMN_DISPLAY_NAME, presetLabel + " " + (preset + 1) + ": " + met)
                .add(Document.COLUMN_MIME_TYPE, Document.MIME_TYPE_DIR)
                .add(Document.COLUMN_FLAGS, 0)
                .add(Document.COLUMN_LAST_MODIFIED, file.lastModified())
                .add(Document.COLUMN_SIZE, file.length());
    }

    @Override
    public Cursor queryDocument(String documentId, String[] projection) {
        final MatrixCursor result = new MatrixCursor(projection == null ? DEFAULT_DOCUMENT_PROJECTION : projection);
        if (documentId.equals(ROOT)) {
            result.newRow()
                    .add(Document.COLUMN_DOCUMENT_ID, ROOT)
                    .add(Document.COLUMN_DISPLAY_NAME, "AAT GPX")
                    .add(Document.COLUMN_MIME_TYPE, Document.MIME_TYPE_DIR)
                    .add(Document.COLUMN_FLAGS, 0);
        }
        if (documentId.startsWith(DIR_PREFIX)) {
            final int preset = Integer.parseInt(documentId.substring(DIR_PREFIX.length()));
            includeDirectory(result, preset);
        }
        if (documentId.startsWith(GPX_PREFIX)) {
            final int sep = documentId.indexOf(GPX_INFIX);
            final int preset = Integer.parseInt(documentId.substring(GPX_PREFIX.length(), sep));
            final String gpx = documentId.substring(sep + GPX_INFIX.length());
            final File file = new File(AppDirectory.getTrackListDirectory(new AndroidSolidDataDirectory(getContext()), preset).descendant(gpx).getPath());
            includeFile(result, preset, file);
        }
        return result;
    }

    @Override
    public Cursor queryChildDocuments(String parentDocumentId, String[] projection, String sortOrder) {
        final Context context = getContext();
        final MatrixCursor result = new MatrixCursor(projection == null ? DEFAULT_DOCUMENT_PROJECTION : projection);
        if (parentDocumentId.equals(ROOT)) {
            final int length = new SolidPresetCount(new Storage(context)).getValue();
            for (int i = 0; i < length; ++i) {
                includeDirectory(result, i);
            }
            addRecentDocuments(result, length);
            return result;
        }
        final int preset = Integer.parseInt(parentDocumentId.substring(DIR_PREFIX.length()));
        final File dir = new File(AppDirectory.getTrackListDirectory(new AndroidSolidDataDirectory(context), preset).getPath());

        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".gpx")) {
                includeFile(result, preset, file);
            }
        }
        return result;
    }

    @Override
    public ParcelFileDescriptor openDocument(String documentId, String mode, @Nullable CancellationSignal signal) throws FileNotFoundException {
        if (!documentId.startsWith(GPX_PREFIX) || mode.contains("w"))
            throw new FileNotFoundException(documentId);
        final int sep = documentId.indexOf(GPX_INFIX);
        final int preset = Integer.parseInt(documentId.substring(GPX_PREFIX.length(), sep));
        final String gpx = documentId.substring(sep + GPX_INFIX.length());
        final Context context = getContext();
        final File file = new File(AppDirectory.getTrackListDirectory(new AndroidSolidDataDirectory(context), preset).descendant(gpx).getPath());
        if (!file.isFile() || !file.getName().endsWith(".gpx"))
            throw new FileNotFoundException(documentId);
        if (!Objects.equals(file.getParentFile(),
                new File(AppDirectory.getTrackListDirectory(new AndroidSolidDataDirectory(context), preset).getPath()))) {
            throw new FileNotFoundException(documentId);
        }
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.parseMode(mode));
    }

    @Override
    public Cursor queryRecentDocuments(String rootId, String[] projection) {
        final Context context = getContext();
        final MatrixCursor result = new MatrixCursor(projection == null ? DEFAULT_DOCUMENT_PROJECTION : projection);
        final int length = new SolidPresetCount(new Storage(context)).getValue();
        addRecentDocuments(result, length);
        return result;
    }

    private void addRecentDocuments(MatrixCursor result, int numPresets) {
        // Add gpx files from the last 24 hours
        final Context context = getContext();
        final long yesterday = new Date().getTime() - 86400000;
        for (int preset = 0; preset < numPresets; ++preset) {
            final File dir = new File(AppDirectory.getTrackListDirectory(new AndroidSolidDataDirectory(context), preset).getPath());
            if (dir.exists() && dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".gpx") && file.lastModified() > yesterday) {
                        includeFile(result, preset, file);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreate() {
        return true;
    }
}
