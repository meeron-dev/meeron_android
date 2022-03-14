package fourtune.merron.data.source.local.provider

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import forutune.meeron.domain.provider.FileProvider
import javax.inject.Inject

class FileProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileProvider {

    override fun getFileName(uriString: String): String {
        val uri: Uri = Uri.parse(uriString)
        context.contentResolver.query(uri, null, null, null, null, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return cursor.getString(index)
            } else {
                throw NullPointerException("not found fileName : $uri")
            }
        }
    }

    override fun getMediaType(uriString: String): String {
        val uri: Uri = Uri.parse(uriString)
        val docId: String = DocumentsContract.getDocumentId(uri)
        val (type) = docId.split(":").toTypedArray()
        return type
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    override fun getPath(uriString: String): String? {
        val uri: Uri = Uri.parse(uriString)
        // DocumentProvider
        return when {
            DocumentsContract.isDocumentUri(context, uri) -> {
                // ExternalStorageProvider
                val docId: String = DocumentsContract.getDocumentId(uri)
                when {
                    isExternalStorageDocument(uri) -> {
                        val (type, id) = docId.split(":").toTypedArray()
                        if ("primary".equals(type, ignoreCase = true)) {
                            Environment.getExternalStorageDirectory().toString() + "/" + id
                        } else null
                    }
                    isDownloadsDocument(uri) -> {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId)
                        )
                        getDataColumn(context, contentUri, null, null)
                    }
                    isMediaDocument(uri) -> {
                        val (type, id) = docId.split(":").toTypedArray()
                        val contentUri = when (type) {
                            "image" -> {
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                            else -> throw IllegalStateException()
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(id)
                        getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                    else -> null
                }
            }
            "content".equals(uri.scheme, ignoreCase = true) -> {
                getDataColumn(context, uri, null, null)
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                uri.path
            }
            else -> null
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

}