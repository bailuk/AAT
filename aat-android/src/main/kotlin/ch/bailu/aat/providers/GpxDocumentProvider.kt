package ch.bailu.aat.providers

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsProvider
import ch.bailu.aat_lib.logger.AppLog
import java.io.File
import java.io.FileNotFoundException

class GpxDocumentProvider : DocumentsProvider() {

    override fun queryRoots(projection: Array<String>?): Cursor {
        AppLog.d(this, "queryRoots")
        return MatrixRoot(Constants.getRootProjection(projection)).newAppRoot().matrix
    }

    override fun queryChildDocuments(
        parentDocumentId: String,
        projection: Array<String>?,
        sortOrder: String
    ): Cursor {
        AppLog.d(this, "queryChildDocuments: $parentDocumentId")

        val context = this.context

        val documentId = DocumentId(parentDocumentId)

        if (context is Context) {
            val providerContext = ProviderContext(context)

            val result =
                MatrixDirectories(Constants.getDocumentProjection(projection), providerContext)

            if (documentId.isRoot && documentId.isValid()) {
                result.includeDirectories()
                result.addRecentDocuments()
            } else {
                result.includeFiles(
                    parentDocumentId.substring(Constants.DIR_PREFIX.length).toInt()
                )
            }

            return result.matrix
        }
        return MatrixCursor(projection)
    }

    override fun queryDocument(documentIdString: String, projection: Array<String>?): Cursor {
        AppLog.d(this, "queryDocument: $documentIdString")

        val context = this.context

        if (context is Context) {
            val providerContext = ProviderContext(context)
            val documentId = DocumentId(documentIdString)

            if (documentId.isRoot && documentId.isValid()) {
                return MatrixRoot(Constants.getDocumentProjection(projection))
                    .newDirectoryRoot().matrix

            } else if (documentId.isDirectory && documentId.isValid()) {
                return MatrixDirectories(
                    Constants.getDocumentProjection(projection),
                    providerContext
                )
                    .includeDirectory(documentId.preset)
                    .matrix

            } else if (documentId.isFile && documentId.isValid()) {
                val file = providerContext.getTrackListDirectory(documentId.preset)
                    .descendant(documentId.fileName)
                return MatrixDirectories(
                    Constants.getDocumentProjection(projection),
                    providerContext
                )
                    .includeFile(file, documentId.preset).matrix
            }
        }
        return MatrixCursor(projection)
    }


    @Throws(FileNotFoundException::class)
    override fun openDocument(
        documentIdString: String,
        mode: String,
        signal: CancellationSignal?
    ): ParcelFileDescriptor {
        AppLog.d(this, "openDocument: $documentIdString")

        val documentId = DocumentId(documentIdString)
        val context = this.context

        if (context is Context && !mode.contains("w") && documentId.isFile && documentId.isValid()) {
            val providerContext = ProviderContext(context)
            val directory = providerContext.getTrackListDirectory(documentId.preset)
            val file =
                File(directory.child(documentId.fileName).path) // Only supports unix files (no SAF support)

            if (file.isFile && file.name.endsWith(".gpx")) {
                return ParcelFileDescriptor.open(file, ParcelFileDescriptor.parseMode(mode))
            }
        }
        throw FileNotFoundException(documentIdString)
    }

    override fun queryRecentDocuments(rootId: String, projection: Array<String>?): Cursor {
        AppLog.d(this, "queryRecentDocuments: $rootId")

        val context = this.context

        if (context is Context) {
            val providerContext = ProviderContext(context)
            val result =
                MatrixDirectories(Constants.getDocumentProjection(projection), providerContext)
            result.addRecentDocuments()
            return result.matrix
        }
        return MatrixCursor(Constants.getDocumentProjection(projection))
    }

    override fun onCreate(): Boolean {
        return true
    }
}
