package ch.bailu.aat.views.osm

import android.content.Context
import android.text.InputType
import android.widget.EditText
import ch.bailu.aat.util.fs.TextBackup
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

class TagEditor(context: Context, an: Foc) : EditText(context) {
    private var backup: Foc = Foc.FOC_NULL

    init {
        createEditor()
        createEditorBackup(an)
    }

    private fun createEditorBackup(directory: Foc) {
        backup = directory.child(AppDirectory.FILE_EDIT_BACKUP)
        showFile()
    }

    private fun createEditor() {
        inputType = (InputType.TYPE_TEXT_FLAG_MULTI_LINE
                or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                or InputType.TYPE_CLASS_TEXT
                or InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
        setHorizontallyScrolling(true)
    }

    public override fun onDetachedFromWindow() {
        saveFile()
        super.onDetachedFromWindow()
    }

    private fun showFile() {
        val text = try {
            TextBackup.read(backup)
        } catch (e: Exception) {
            ""
        }
        setText(text)
    }

    private fun saveFile() {
        try {
            TextBackup.write(backup, text.toString())
        } catch (e: Exception) {
            AppLog.e(this@TagEditor, e)
        }
    }
}
