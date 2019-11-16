package ch.bailu.aat.views;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import ch.bailu.aat.util.TextBackup;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;


public class TagEditor extends EditText {
    private Foc backup;


    public TagEditor(Context context, Foc an)  {
        super(context);

        createEditor();
        createEditorBackup(an);
    }

    private void createEditorBackup(Foc directory) {
        backup = directory.child("edit.txt");
        showFile();
    }


    private void createEditor() {
        setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        setHorizontallyScrolling(true);
    }



    @Override
    public void onDetachedFromWindow() {
        saveFile();
        super.onDetachedFromWindow();
    }


    private void showFile() {
        String text;
        try {
            text = TextBackup.read(backup);

        } catch (Exception e) {
            text = "";
        }
        setText(text);

    }


    private void saveFile() {
        try {
            TextBackup.write(backup, getText().toString());
        } catch (Exception e) {
            AppLog.e(getContext(), TagEditor.this, e);

        }
    }


}
